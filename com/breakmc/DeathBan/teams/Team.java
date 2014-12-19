package com.breakmc.DeathBan.teams;

import java.util.*;
import org.bukkit.*;

public class Team
{
    String name;
    String leader;
    List<String> managers;
    List<String> members;
    Location home;
    boolean friendlyFire;
    String password;
    
    public Team(final String name, final String leader, final List<String> managers, final List<String> members) {
        this(name, leader, managers, members, "");
    }
    
    public Team(final String name, final String leader, final List<String> managers, final List<String> members, final String password) {
        super();
        this.name = name;
        this.leader = leader;
        this.managers = managers;
        this.members = members;
        this.friendlyFire = false;
        this.password = password;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getLeader() {
        return this.leader;
    }
    
    public List<String> getManagers() {
        return this.managers;
    }
    
    public List<String> getMembers() {
        return this.members;
    }
    
    public Location getHome() {
        return this.home;
    }
    
    public boolean isFriendlyFire() {
        return this.friendlyFire;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLeader(final String leader) {
        this.leader = leader;
    }
    
    public void setManagers(final List<String> managers) {
        this.managers = managers;
    }
    
    public void setMembers(final List<String> members) {
        this.members = members;
    }
    
    public void setHome(final Location home) {
        this.home = home;
    }
    
    public void setFriendlyFire(final boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        final Team other = (Team)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$leader = this.getLeader();
        final Object other$leader = other.getLeader();
        Label_0102: {
            if (this$leader == null) {
                if (other$leader == null) {
                    break Label_0102;
                }
            }
            else if (this$leader.equals(other$leader)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$managers = this.getManagers();
        final Object other$managers = other.getManagers();
        Label_0139: {
            if (this$managers == null) {
                if (other$managers == null) {
                    break Label_0139;
                }
            }
            else if (this$managers.equals(other$managers)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$members = this.getMembers();
        final Object other$members = other.getMembers();
        Label_0176: {
            if (this$members == null) {
                if (other$members == null) {
                    break Label_0176;
                }
            }
            else if (this$members.equals(other$members)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$home = this.getHome();
        final Object other$home = other.getHome();
        Label_0213: {
            if (this$home == null) {
                if (other$home == null) {
                    break Label_0213;
                }
            }
            else if (this$home.equals(other$home)) {
                break Label_0213;
            }
            return false;
        }
        if (this.isFriendlyFire() != other.isFriendlyFire()) {
            return false;
        }
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null) {
            if (other$password == null) {
                return true;
            }
        }
        else if (this$password.equals(other$password)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Team;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 0 : $name.hashCode());
        final Object $leader = this.getLeader();
        result = result * 59 + (($leader == null) ? 0 : $leader.hashCode());
        final Object $managers = this.getManagers();
        result = result * 59 + (($managers == null) ? 0 : $managers.hashCode());
        final Object $members = this.getMembers();
        result = result * 59 + (($members == null) ? 0 : $members.hashCode());
        final Object $home = this.getHome();
        result = result * 59 + (($home == null) ? 0 : $home.hashCode());
        result = result * 59 + (this.isFriendlyFire() ? 79 : 97);
        final Object $password = this.getPassword();
        result = result * 59 + (($password == null) ? 0 : $password.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Team(name=" + this.getName() + ", leader=" + this.getLeader() + ", managers=" + this.getManagers() + ", members=" + this.getMembers() + ", home=" + this.getHome() + ", friendlyFire=" + this.isFriendlyFire() + ", password=" + this.getPassword() + ")";
    }
}
