package com.github.charlyb01.xpstorage_trinkets.mixin;

import com.github.charlyb01.xpstorage.BookInfo;
import com.github.charlyb01.xpstorage.Utils;
import com.github.charlyb01.xpstorage.XpBook;
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.github.charlyb01.xpstorage.component.XpAmountData;
import com.github.charlyb01.xpstorage_trinkets.XpstorageTrinkets;
import com.github.charlyb01.xpstorage_trinkets.config.ModConfig;
import com.mojang.authlib.GameProfile;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity {
    public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void saveXPOnDeath(DamageSource source, CallbackInfo ci) {
        ItemStack xpSaver = this.getXPSaver();
        if (xpSaver == null) return;

        if (xpSaver.getDamage() < xpSaver.getMaxDamage()) {
            List<ItemStack> xpBooks = this.getXPBooks();
            boolean damageTrinket = false;
            float transferPenalty = ModConfig.get().xpSaverTransfer / 100.f;
            int playerExperience = Math.round(Utils.getPlayerExperience(this) * transferPenalty);

            while (!xpBooks.isEmpty() && playerExperience > 0) {
                ItemStack xpBook = xpBooks.getFirst();
                int bookExperience = xpBook.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY).amount();
                int bookMaxExperience = ((BookInfo) xpBook.getItem()).getMaxExperience();
                int bookMaxLevel = ((BookInfo) xpBook.getItem()).getMaxLevel();
                int bookRemainingExperience = bookMaxExperience - bookExperience;
                if (bookRemainingExperience == 0) {
                    xpBooks.removeFirst();
                    continue;
                }

                if (!damageTrinket) {
                    damageTrinket = true;
                }

                // Check max value
                if (bookRemainingExperience < playerExperience) {
                    this.addExperience(-bookRemainingExperience);
                    playerExperience -= bookRemainingExperience;
                    xpBook.set(MyComponents.XP_COMPONENT, new XpAmountData(bookMaxExperience, bookMaxLevel));
                } else {
                    int amount = bookExperience + playerExperience;
                    xpBook.set(MyComponents.XP_COMPONENT, new XpAmountData(amount, Utils.getLevelFromExperience(amount)));
                    this.addExperience(-playerExperience);
                    playerExperience = 0;
                }
            }

            if (damageTrinket) {
                xpSaver.setDamage(xpSaver.getDamage() + 1);
            }
        }
    }

    @Unique
    private ItemStack getXPSaver() {
        if (TrinketsApi.getTrinketComponent(this).isEmpty()) return null;

        List<Pair<SlotReference, ItemStack>> slots = TrinketsApi.getTrinketComponent(this).get()
                .getEquipped(XpstorageTrinkets.xp_saver);

        return slots.isEmpty() ? null : slots.getFirst().getRight();
    }

    @Unique
    private List<ItemStack> getXPBooks() {
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack itemStack : this.getInventory().main) {
            if (itemStack.getItem() instanceof XpBook) {
                list.add(itemStack);
            }
        }

        return list;
    }
}
