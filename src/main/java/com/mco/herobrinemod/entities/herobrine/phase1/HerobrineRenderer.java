package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

import static net.minecraft.client.model.geom.PartNames.LEFT_HAND;
import static net.minecraft.client.model.geom.PartNames.RIGHT_HAND;

public class HerobrineRenderer extends GeoEntityRenderer<Herobrine> {
	public HerobrineRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new HerobrineModel<>());
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
		this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
			@Nullable
			@Override
			protected ItemStack getStackForBone(GeoBone bone, Herobrine animatable) {
				// Retrieve the items in the entity's hands for the relevant bone
				ItemStack stack = new ItemStack(Items.AIR);
				if ("leftArm2".equals(bone.getName()))
					stack = new ItemStack(Items.DIAMOND_SWORD);
				return stack;
			}

			@Override
			protected ItemTransforms.TransformType getTransformTypeForStack(GeoBone bone, ItemStack stack, Herobrine animatable) {
				// Apply the camera transform for the given hand
				return switch (bone.getName()) {
					case LEFT_HAND, RIGHT_HAND -> ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
					default -> ItemTransforms.TransformType.NONE;
				};
			}

			// Do some quick render modifications depending on what the item is
			@Override
			protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, Herobrine animatable,
											  MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
				if("leftArm2".equals(bone.getName())) {
					poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
					poseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
					poseStack.translate(0.50F, 0.10F, 0.0F);

					super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
				}
			}
		});
	}

}
