package com.github.charlyb01.xpstorage_trinkets.mixin;

import com.github.charlyb01.xpstorage.BookInfo;
import com.github.charlyb01.xpstorage.XpBook;
import com.github.charlyb01.xpstorage.cardinal.MyComponents;
import com.github.charlyb01.xpstorage_trinkets.XpstorageTrinkets;
import com.github.charlyb01.xpstorage_trinkets.config.ModConfig;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = PlayerEntity.class, priority = 100)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public abstract PlayerInventory getInventory();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "addExperience", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int putXPOnConduit(int experience) {
        if (this.getMainHandStack().getItem() instanceof XpBook
                || this.getOffHandStack().getItem() instanceof XpBook) {
            return experience;
        }

        List<ItemStack> xpConduits = this.getXPConduits();
        List<ItemStack> xpBooks = this.getXPBooks();
        int toTransfer = 0;

        if (!xpBooks.isEmpty()) {
            toTransfer = Math.round(experience * (ModConfig.get().xpConduitTransfer / 100.f));
            experience -= toTransfer;
        }

        while (!xpBooks.isEmpty()
                && !xpConduits.isEmpty()
                && toTransfer > 0) {

            ItemStack xpBook = xpBooks.get(0);
            int bookExperience = MyComponents.XP_COMPONENT.get(xpBook).getAmount();
            int bookMaxExperience = ((BookInfo) xpBook.getItem()).getMaxExperience();
            int bookRemainingExperience = bookMaxExperience - bookExperience;
            if (bookRemainingExperience == 0) {
                xpBooks.remove(0);
                continue;
            }

            ItemStack xpConduit = xpConduits.get(0);
            int conduitDamage = xpConduit.getDamage();
            int conduitDurability = xpConduit.getMaxDamage() - conduitDamage;
            if (conduitDurability == 0) {
                xpConduits.remove(0);
                continue;
            }

            int toRemove = Math.min(Math.min(bookRemainingExperience, conduitDurability), toTransfer);
            toTransfer -= toRemove;
            MyComponents.XP_COMPONENT.get(xpBook).setAmount(bookExperience + toRemove);
            xpConduit.setDamage(conduitDamage + toRemove);
        }

        experience += toTransfer;
        return experience;
    }

    private List<ItemStack> getXPConduits() {
        List<ItemStack> list = new ArrayList<>();

        if (TrinketsApi.getTrinketComponent(this).isPresent()) {
            List<Pair<SlotReference, ItemStack>> slots = TrinketsApi.getTrinketComponent(this).get()
                    .getEquipped(XpstorageTrinkets.xp_conduit);
            slots.forEach(slot -> list.add(slot.getRight()));
        }

        return list;
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
