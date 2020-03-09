package com.mco.herobrinemod.entities.util;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.function.Function;

public class AdvancedLibModelBase extends AdvancedModelBase
{
    public List<AdvancedModelRenderer> listArmHierarchy = null;
    private HashMap<AdvancedModelRenderer, AdvancedModelRenderer> lookupChildToParent = new HashMap<>();
    public HashMap<AdvancedModelRenderer, Vec3d> lookupModelToDefaultRotations = new HashMap<>();

    public float extraScaleForRender = 1F;

    public void staggerWaggle(float ageInTicks, Vec3d dimensions, Function<Float, Float> function, float speed, int timeStagger, float ampAntiStagger, AdvancedModelRenderer... models) {
        float index = 1;
        for (AdvancedModelRenderer model : models) {
            //wing_1_right.rotateAngleX += MathHelper.sin((ageInTicks) * 0.067F) * range * 1F;
            if (dimensions.x != 0) {
                model.rotateAngleX += function.apply((ageInTicks - (timeStagger * (index - 1))) * speed) * dimensions.x * (ampAntiStagger * index);
            }
            if (dimensions.y != 0) {
                model.rotateAngleY += function.apply((ageInTicks - (timeStagger * (index - 1))) * speed) * dimensions.y * (ampAntiStagger * index);
            }
            if (dimensions.z != 0) {
                model.rotateAngleZ += function.apply((ageInTicks - (timeStagger * (index - 1))) * speed) * dimensions.z * (ampAntiStagger * index);
            }
            index++;
        }
    }

    public AdvancedModelRenderer getModelForItem() {
        return null;
    }

    public void resetRotations() {
        for (Map.Entry<AdvancedModelRenderer, Vec3d> entry : lookupModelToDefaultRotations.entrySet()) {
            entry.getKey().rotateAngleX = (float)entry.getValue().x;
            entry.getKey().rotateAngleY = (float)entry.getValue().y;
            entry.getKey().rotateAngleZ = (float)entry.getValue().z;
        }
    }

    public void mapModelRotations(AdvancedModelRenderer root) {
        recurseModel(root);
    }

    private void recurseModel(AdvancedModelRenderer parent) {
        if (parent.childModels != null && parent.childModels.size() > 0) {
            for (ModelRenderer child : parent.childModels) {
                if (!lookupModelToDefaultRotations.containsKey(child)) {
                    lookupModelToDefaultRotations.put((AdvancedModelRenderer) child, new Vec3d(child.rotateAngleX, child.rotateAngleY, child.rotateAngleZ));
                }

                if (child.childModels != null && child.childModels.size() > 0) {
                    recurseModel((AdvancedModelRenderer) child);
                }
            }
        }
    }

    public List<AdvancedModelRenderer> getArmHierarchyFromBody() {
        //since AdvancedModelRenderer doesnt provide parent lookup, we have to generate one ourselves the hard way
        if (listArmHierarchy == null) {
            listArmHierarchy = new ArrayList<>();
            AdvancedModelRenderer armToUse = getModelForItem();
            if (armToUse != null) {
                lookupChildToParent = new HashMap<>();
                for (ModelRenderer modelPart : this.boxList) {
                    if (modelPart.childModels != null) {
                        for (ModelRenderer modelChild : modelPart.childModels) {
                            lookupChildToParent.put((AdvancedModelRenderer)modelChild, (AdvancedModelRenderer)modelPart);
                        }
                    }
                }

                listArmHierarchy.add(armToUse);
                AdvancedModelRenderer nextParent = lookupChildToParent.get(armToUse);
                while (nextParent != null) {
                    listArmHierarchy.add(nextParent);
                    nextParent = lookupChildToParent.get(nextParent);
                }

                Collections.reverse(listArmHierarchy);
            } else {
                return new ArrayList<>();
            }
        }
        return listArmHierarchy;
    }

    public AdvancedModelRenderer getParent(AdvancedModelRenderer renderer) {
        //init it
        getArmHierarchyFromBody();
        return lookupChildToParent.get(renderer);
    }

    public void translateToHand() {
    }

    public float getShadowScale() {
        return 1F;
    }

    public void tiltModelPiece(AdvancedModelRenderer model, Entity entity, float scale) {
        model.rotateAngleX = (float)Math.toRadians(entity.rotationPitch) * scale;
    }

    public void spinModelPieceX(AdvancedModelRenderer model, Entity entity, float limbSwing, float speedScale) {
        spinModelPiece(model, new Vec3d(1, 0, 0), entity, limbSwing, speedScale);
    }

    public void spinModelPieceY(AdvancedModelRenderer model, Entity entity, float limbSwing, float speedScale) {
        spinModelPiece(model, new Vec3d(0, 1, 0), entity, limbSwing, speedScale);
    }

    public void spinModelPieceZ(AdvancedModelRenderer model, Entity entity, float limbSwing, float speedScale) {
        spinModelPiece(model, new Vec3d(0, 0, 1), entity, limbSwing, speedScale);
    }

    private void spinModelPiece(AdvancedModelRenderer model, Vec3d vec, Entity entity, float limbSwing, float speedScale) {

        float moveForward = ((EntityLivingBase) entity).moveForward;
        float val;

        boolean isForward = moveForward >= 0;
        val = isForward ? limbSwing : -limbSwing;

        if (vec.x != 0) {
            model.rotateAngleX += val * vec.x * speedScale;
        } else if (vec.y != 0) {
            model.rotateAngleY += val * vec.y * speedScale;
        } else if (vec.z != 0) {
            model.rotateAngleZ += val * vec.z * speedScale;
        }
    }
}

