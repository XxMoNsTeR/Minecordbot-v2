package us.cyrien.minecordbot.core.module;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.entity.MCBUser;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.localization.Localization;
import us.cyrien.minecordbot.Minecordbot;

import java.awt.*;
import java.util.List;

public abstract class DiscordCommand implements Comparable {

    public static final int HELP_COMMAND_DURATION = 30;

    private String name;
    private List<String> aliases;
    protected String description;
    protected String usage;
    protected PermissionLevel permission;

    private MCBUser sender;
    private Messenger messenger;
    private CommandType commandType;
    private boolean nullified;
    private Minecordbot mcb;

    protected DiscordCommand(String name, String description, String usageMessage, List<String> aliases, CommandType commandType) {
        this.name = name;
        this.description = Localization.getTranslatedMessage(description);
        this.usage = Localization.getTranslatedMessage(usageMessage);
        this.aliases = aliases;
        this.commandType = commandType;
        this.messenger = Minecordbot.getMessenger();
        this.nullified = false;
        this.mcb = Minecordbot.getInstance();
    }

    public abstract boolean execute(MessageReceivedEvent var1, String[] var2);

    public String getName() {
        return this.name;
    }

    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    public void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me, int duration) {
        messenger.sendCommandEmbedResponse(e, me, duration);
    }

    public void sendMessage(MessageReceivedEvent e, String message, int duration) {
        messenger.sendCommandResponse(e, message, duration);
    }

    public MessageEmbed getHelpCard(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(this.getName().substring(0, 1).toUpperCase() + this.getName().substring(1) + " Command Help Card:", null);
        eb.addField("Usage", MCBConfig.get("trigger") + getUsage(), false);
        String r;
        if (this.getAliases().size() == 0 || this.getAliases() == null)
            r = Localization.getTranslatedMessage("mcb.command.no-alias");
        else
            r = this.getAliases().toString().replaceAll("\\[", "").replaceAll("]", "");
        eb.addField("Description", getDescription(), false);
        eb.addField("Alias", r, false);
        MCBUser user = getSender();
        PermissionLevel rp = this.getPermission();
        PermissionLevel sp = user.getPermissionLevel();
        eb.addField("Permission", "Required Level: " + rp + "\nYour Level: " + sp, false);
        return eb.build();
    }

    public Minecordbot getMCB() {
        return mcb;
    }

    public MessageEmbed getInvalidHelpCard(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getHelpCard(e));
        eb.setColor(new Color(217, 83, 79));
        return eb.build();
    }

    public String noPermissionMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.no-perm-message") + "`";
    }

    public MessageEmbed noPermissionMessageEmbed() {
        String s = Localization.getTranslatedMessage("mcb.command.no-perm-message");
        EmbedBuilder eb = new EmbedBuilder().setTitle(s, null);
        return eb.build();
    }

    public String invalidArgumentsMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.invalid-arguments") + "`";
    }

    public MessageEmbed invalidArgumentsMessageEmbed() {
        String s = Localization.getTranslatedMessage("mcb.command.no-perm-message");
        EmbedBuilder eb = new EmbedBuilder().setTitle(s, null);
        return eb.build();
    }

    public String invalidTcMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.invalid-tc") + "`";
    }

    public MessageEmbed invalidTcMessageEmbed() {
        String s =Localization.getTranslatedMessage("mcb.command.invalid-tc") ;
        EmbedBuilder eb = new EmbedBuilder().setTitle(s, null);
        return eb.build();
    }

    public PermissionLevel getPermission() {
        return this.permission;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public void setPermission(PermissionLevel permission) {
        this.permission = permission;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public MCBUser getSender() {
        return sender;
    }

    public boolean isNullified() {
        return nullified;
    }

    public DiscordCommand nullify(String psuedoName) {
        this.name = psuedoName;
        this.description = null;
        this.usage = null;
        this.aliases = null;
        this.commandType = null;
        nullified = true;
        return this;
    }

    public DiscordCommand setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public DiscordCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    public DiscordCommand setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public DiscordCommand setSender(MCBUser sender) {
        this.sender = sender;
        return this;
    }

    public String toString() {
        return this.getClass().getName() + '(' + this.name + ')';
    }

    @Override
    public int compareTo(Object o) {
        DiscordCommand temp = (DiscordCommand) o;
        return this.getName().compareTo(temp.getName());
    }

}