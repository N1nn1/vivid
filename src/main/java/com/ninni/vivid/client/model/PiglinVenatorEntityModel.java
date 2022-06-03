package com.ninni.vivid.client.model;

import com.google.common.collect.ImmutableList;
import com.ninni.vivid.entity.PiglinVenatorEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings("FieldCanBeLocal, unused")
public class PiglinVenatorEntityModel extends AnimalModel<PiglinVenatorEntity> {
    private final ModelPart root;

    private final ModelPart R_Leg;
    private final ModelPart R_Leg_Layer;
    private final ModelPart L_Leg;
    private final ModelPart L_Leg_Layer;
    private final ModelPart Torso;
    private final ModelPart Torso_Layer;
    private final ModelPart Head;
    private final ModelPart R_Ear;
    private final ModelPart L_Ear;
    private final ModelPart knot;
    private final ModelPart R_Arm;
    private final ModelPart R_Arm_Layer;
    private final ModelPart R_Chains;
    private final ModelPart L_Arm;
    private final ModelPart L_Arm_Layer;
    private final ModelPart L_Chains;

    public PiglinVenatorEntityModel(ModelPart root) {
        this.root = root;

        this.Torso         = root.getChild("Torso");
        this.L_Leg         = root.getChild("L_Leg");
        this.L_Leg_Layer   = L_Leg.getChild("L_Leg_Layer");
        this.R_Leg         = root.getChild("R_Leg");
        this.R_Leg_Layer   = R_Leg.getChild("R_Leg_Layer");

        this.Torso_Layer   = Torso.getChild("Torso_Layer");
        this.Head          = Torso.getChild("Head");
        this.L_Arm         = Torso.getChild("L_Arm");
        this.L_Arm_Layer   = L_Arm.getChild("L_Arm_Layer");
        this.L_Chains      = L_Arm_Layer.getChild("L_Chains");
        this.R_Arm         = Torso.getChild("R_Arm");
        this.R_Arm_Layer   = R_Arm.getChild("R_Arm_Layer");
        this.R_Chains      = R_Arm_Layer.getChild("R_Chains");

        this.knot          = Head.getChild("knot");
        this.L_Ear         = Head.getChild("L_Ear");
        this.R_Ear         = Head.getChild("R_Ear");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData R_Leg = root.addChild(
            "R_Leg",
            ModelPartBuilder.create()
                            .uv(16, 48)
                            .mirrored(false)
                            .cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(-2.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData R_Leg_Layer = R_Leg.addChild(
            "R_Leg_Layer",
            ModelPartBuilder.create()
                            .uv(0, 48)
                            .mirrored(false)
                            .cuboid(-2.0F, 0.0F, -1.75F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)),
            ModelTransform.of(0.1F, 0.0F, -0.15F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData L_Leg = root.addChild(
            "L_Leg",
            ModelPartBuilder.create()
                            .uv(0, 16)
                            .mirrored(false)
                            .cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(2.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData L_Leg_Layer = L_Leg.addChild(
            "L_Leg_Layer",
            ModelPartBuilder.create()
                            .uv(0, 32)
                            .mirrored(false)
                            .cuboid(-2.0F, 0.0F, -2.1F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)),
            ModelTransform.of(-0.1F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData Torso = root.addChild(
            "Torso",
            ModelPartBuilder.create()
                            .uv(16, 16)
                            .mirrored(false)
                            .cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -11.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData Torso_Layer = Torso.addChild(
            "Torso_Layer",
            ModelPartBuilder.create()
                            .uv(16, 32)
                            .mirrored(false)
                            .cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)),
            ModelTransform.of(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData Head = Torso.addChild(
            "Head",
            ModelPartBuilder.create()
                            .uv(0, 0)
                            .mirrored(false)
                            .cuboid(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new Dilation(0.0F))
                            .uv(31, 1)
                            .mirrored(false)
                            .cuboid(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
                            .uv(2, 4)
                            .mirrored(false)
                            .cuboid(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                            .uv(2, 0)
                            .mirrored(false)
                            .cuboid(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData R_Ear = Head.addChild(
            "R_Ear",
            ModelPartBuilder.create()
                            .uv(51, 6)
                            .mirrored(false)
                            .cuboid(-1.0F, 0.0F, -2.0F, 1.0F, 6.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, 0.6036F)
        );

        ModelPartData L_Ear = Head.addChild(
            "L_Ear",
            ModelPartBuilder.create()
                            .uv(39, 6)
                            .mirrored(false)
                            .cuboid(0.0F, 0.0F, -2.0F, 1.0F, 6.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, -0.6036F)
        );

        ModelPartData knot = Head.addChild(
            "knot",
            ModelPartBuilder.create()
                            .uv(52, 0)
                            .mirrored(false)
                            .cuboid(-3.0F, 0.0F, 0.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -6.0F, 4.0F, 0.7854F, 0.0F, 0.0F)
        );

        ModelPartData R_Arm = Torso.addChild(
            "R_Arm",
            ModelPartBuilder.create()
                            .uv(32, 48)
                            .mirrored(false)
                            .cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(-5.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData R_Arm_Layer = R_Arm.addChild(
            "R_Arm_Layer",
            ModelPartBuilder.create()
                            .uv(48, 48)
                            .mirrored(false)
                            .cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)),
            ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData R_Chains = R_Arm_Layer.addChild(
            "R_Chains",
            ModelPartBuilder.create()
                            .uv(0, 64)
                            .mirrored(false)
                            .cuboid(0.0F, 0.0F, -2.0F, 1.0F, 16.0F, 4.0F, new Dilation(0.125F)),
            ModelTransform.of(-3.0F, 6.0F, 0.0F, 0.2618F, 0.0F, 0.0F)
        );

        ModelPartData L_Arm = Torso.addChild(
            "L_Arm",
            ModelPartBuilder.create()
                            .uv(40, 16)
                            .mirrored(false)
                            .cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)),
            ModelTransform.of(5.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData L_Arm_Layer = L_Arm.addChild(
            "L_Arm_Layer",
            ModelPartBuilder.create()
                            .uv(40, 32)
                            .mirrored(false)
                            .cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)),
            ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData L_Chains = L_Arm_Layer.addChild(
            "L_Chains",
            ModelPartBuilder.create()
                            .uv(10, 64)
                            .mirrored(false)
                            .cuboid(-1.0F, 0.0F, -2.0F, 1.0F, 16.0F, 4.0F, new Dilation(0.125F)),
            ModelTransform.of(3.0F, 6.0F, 0.0F, 0.2618F, 0.0F, 0.0F)
        );

        return TexturedModelData.of(data, 64, 96);
    }

    @Override
    public void setAngles(PiglinVenatorEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        limbDistance = MathHelper.clamp(limbDistance, -0.45F, 0.45F);

        float speed = 1.0F;
        float degree = 1.0F;
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() { return ImmutableList.of(); }

    @Override
    protected Iterable<ModelPart> getBodyParts() { return ImmutableList.of(); }
}
