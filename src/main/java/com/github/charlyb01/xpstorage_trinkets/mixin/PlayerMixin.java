package com.github.charlyb01.xpstorage_trinkets.mixin;

import com.github.charlyb01.xpstorage.Utils;
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.github.charlyb01.xpstorage.component.XpAmountData;
import com.github.charlyb01.xpstorage.item.XpBook;
import com.github.charlyb01.xpstorage_trinkets.item.ItemRegistry;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

import static com.github.charlyb01.xpstorage.item.ItemRegistry.XP_BOOK;

@Mixin(value = PlayerEntity.class, priority = 100)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public abstract PlayerInventory getInventory();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "addExperience", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int putXPOnConduit(int experience) {
        if (this.getMainHandStack().isOf(XP_BOOK)
                || this.getOffHandStack().isOf(XP_BOOK)) {
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

            ItemStack xpBook = xpBooks.getFirst();
            int bookExperience = xpBook.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY).amount();
            int bookMaxExperience = XpBook.getMaxXpAmount(xpBook);
            int bookRemainingExperience = bookMaxExperience - bookExperience;
            if (bookRemainingExperience == 0) {
                xpBooks.removeFirst();
                continue;
            }

            ItemStack xpConduit = xpConduits.getFirst();
            int conduitDamage = xpConduit.getDamage();
            int conduitDurability = xpConduit.getMaxDamage() - conduitDamage;
            if (conduitDurability == 0) {
                xpConduits.removeFirst();
                continue;
            }

            int toRemove = Math.min(Math.min(bookRemainingExperience, conduitDurability), toTransfer);
            toTransfer -= toRemove;
            int amount = bookExperience + toRemove;
            xpBook.set(MyComponents.XP_COMPONENT, new XpAmountData(amount, Utils.getLevelFromExperience(amount)));
            xpConduit.setDamage(conduitDamage + toRemove);
        }

        experience += toTransfer;
        return experience;
    }

    @Unique
    private List<ItemStack> getXPConduits() {
        List<ItemStack> list = new ArrayList<>();

        if (TrinketsApi.getTrinketComponent(this).isPresent()) {
            List<Pair<SlotReference, ItemStack>> slots = TrinketsApi.getTrinketComponent(this).get()
                    .getEquipped(ItemRegistry.XP_CONDUIT);
            slots.forEach(slot -> list.add(slot.getRight()));
        }

        return list;
    }

    @Unique
    private List<ItemStack> getXPBooks() {
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack itemStack : this.getInventory().main) {
            if (itemStack.isOf(XP_BOOK)) {
                list.add(itemStack);
            }
        }

        return list;
    }
}
