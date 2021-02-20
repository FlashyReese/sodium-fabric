package me.jellysquid.mods.sodium.client.gui.options;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionPage {
    private final String name;
    private final ImmutableList<OptionGroup> groups;
    private final ImmutableList<Option<?>> options;

    public OptionPage(String name, ImmutableList<OptionGroup> groups) {
        this.name = name;
        this.groups = groups;

        ImmutableList.Builder<Option<?>> builder = ImmutableList.builder();

        for (OptionGroup group : groups) {
            builder.addAll(group.getOptions());
        }

        this.options = builder.build();
    }

    public ImmutableList<OptionGroup> getGroups() {
        return this.groups;
    }

    public ImmutableList<Option<?>> getOptions() {
        return this.options;
    }

    public String getName() {
        return this.name;
    }

    public static Builder createBuilder(){
        return new Builder();
    }

    public static class Builder{
        private String name;
        private final List<OptionGroup> groups = new ArrayList<>();

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder addOptionGroup(OptionGroup group){
            this.groups.add(group);
            return this;
        }

        public Builder addOptionGroups(OptionGroup... groups){
            this.groups.addAll(Arrays.asList(groups));
            return this;
        }

        public Builder addOptionGroups(List<OptionGroup> groups){
            this.groups.addAll(groups);
            return this;
        }

        public OptionPage build(){
            return new OptionPage(this.name, ImmutableList.copyOf(this.groups));
        }
    }
}
