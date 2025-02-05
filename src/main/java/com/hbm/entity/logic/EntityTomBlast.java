package com.hbm.entity.logic;

import org.apache.logging.log4j.Level;

import com.hbm.config.GeneralConfig;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.explosion.ExplosionTom;
import com.hbm.main.MainRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EntityTomBlast extends Entity {

	public int age = 0;
	public int destructionRange = 0;
	public ExplosionTom exp;
	public int speed = 1;
	public boolean did = false;
	
	public EntityTomBlast(World worldIn) {
		super(worldIn);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
    	
        if(!this.did)
        {
    		if(GeneralConfig.enableExtendedLogging && !world.isRemote)
    			MainRegistry.logger.log(Level.INFO, "[NUKE] Initialized TOM explosion at " + posX + " / " + posY + " / " + posZ + " with strength " + destructionRange + "!");
    		
        	exp = new ExplosionTom((int)this.posX, (int)this.posY, (int)this.posZ, this.world, this.destructionRange);
        	
        	this.did = true;
        }
        
        speed += 1;	//increase speed to keep up with expansion
        
        boolean flag = false;
        for(int i = 0; i < this.speed; i++)
        {
        	flag = exp.update();
        	
        	if(flag) {
        		this.setDead();
        	}
        }
        
    	if(rand.nextInt(5) == 0)
        	this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
        	
        if(!flag)
        {
        	this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
        	ExplosionNukeGeneric.dealDamage(this.world, this.posX, this.posY, this.posZ, this.destructionRange * 2);
        }
        
        age++;
	}
	
	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		age = nbt.getInteger("age");
		destructionRange = nbt.getInteger("destructionRange");
		speed = nbt.getInteger("speed");
		did = nbt.getBoolean("did");
    	
		exp = new ExplosionTom((int)this.posX, (int)this.posY, (int)this.posZ, this.world, this.destructionRange);
		exp.readFromNbt(nbt, "exp_");
    	
    	this.did = true;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("age", age);
		nbt.setInteger("destructionRange", destructionRange);
		nbt.setInteger("speed", speed);
		nbt.setBoolean("did", did);
    	
		if(exp != null)
			exp.saveToNbt(nbt, "exp_");
	}

}
