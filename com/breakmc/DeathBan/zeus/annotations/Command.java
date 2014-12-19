package com.breakmc.DeathBan.zeus.annotations;

import java.lang.annotation.*;
import javax.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Command {
    @Nonnull
    String name() default "";
    
    String[] aliases() default {};
    
    String desc() default "";
    
    String usage() default "";
    
    String permission() default "";
    
    String permissionMsg() default "";
    
    int minArgs() default 0;
    
    int maxArgs() default -1;
}
