package com.github.charlyb01.xpstorage_trinkets.mixin;

import com.github.charlyb01.xpstorage.Utils;
import com.github.charlyb01.xpstorage.XpBook;
import com.github.charlyb01.xpstorage_trinkets.XpstorageTrinkets;
import com.mojang.authlib.GameProfile;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity {
    public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void saveXPOnDeath(DamageSource source, CallbackInfo ci) {
        if (TrinketsApi.getTrinketComponent(this).isPresent()
                && TrinketsApi.getTrinketComponent(this).get().isEquipped(XpstorageTrinkets.xp_saver)) {

            ItemStack xpSaver = getXPSaver();

            if (xpSaver.getDamage() < xpSaver.getMaxDamage()) {
                int playerExperience = Utils.getExperienceToLevel(this.experienceLevel);
                playerExperience += this.experienceProgress * Utils.getLevelExperience(this.experienceLevel);

                List<ItemStack> xpBooks = getXPBooks();
                if (!xpBooks.isEmpty()) {
                    xpSaver.setDamage(xpSaver.getDamage() + 1);
                }

                while (!xpBooks.isEmpty()
                        && playerExperience > 0) {

                    ItemStack xpBook = xpBooks.get(0);
                    int bookExperience = xpBook.getDamage();
                    int bookMaxExperience = xpBook.getMaxDamage();
                    int bookDurability = bookMaxExperience - bookExperience;
                    if (bookDurability == 0) {
                        xpBooks.remove(0);
                        continue;
                    }

                    // Check max value
                    if (bookMaxExperience - bookExperience < playerExperience) {
                        this.addExperience(bookExperience - bookMaxExperience);
                        playerExperience += bookExperience - bookMaxExperience;
                        xpBook.setDamage(bookMaxExperience);
                    } else {

                        xpBook.setDamage(bookExperience + playerExperience);
                        this.addExperience(-playerExperience);
                        playerExperience = 0;
                        this.experienceProgress = 0.0F;
                    }
                }
            }
        }
    }

    private ItemStack getXPSaver() {
        Map<String, TrinketInventory> group = TrinketsApi.getTrinketComponent(this).get().getInventory().get("chest");
        TrinketInventory slotInventory = group.get("necklace");

        for (int i = 0; i < slotInventory.size(); i++) {
            if (slotInventory.getStack(i).getItem().equals(XpstorageTrinkets.xp_saver)) {
                return slotInventory.getStack(i);
            }
        }

        return null;
    }

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
