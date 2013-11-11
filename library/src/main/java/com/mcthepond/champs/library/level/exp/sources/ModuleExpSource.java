package com.mcthepond.champs.library.level.exp.sources;

import com.mcthepond.champs.library.level.exp.Exp;

/**
 * @author YoshiGenius
 */
public class ModuleExpSource extends ExpSource {

    private Exp exp;

    public ModuleExpSource(Exp exp) {
        super(ExpSourceType.PLUGIN);
        this.exp = exp;
    }

    public ModuleExpSource(double exp) {
        this(new Exp(exp));
    }

    public void setExp(Exp exp) {
        this.exp = exp;
    }

    public Exp getExp() {
        return this.exp;
    }

}
