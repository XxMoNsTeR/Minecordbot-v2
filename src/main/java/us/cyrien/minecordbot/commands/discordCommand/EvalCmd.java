package us.cyrien.minecordbot.commands.discordCommand;

import us.cyrien.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalCmd extends MCBCommand {

    public EvalCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "eval";
        this.arguments = "<script...>";
        this.help = "js eval function";
        this.aliases = new String[]{"ev"};
        this.ownerCommand = true;
        this.category = minecordbot.OWNER;
    }

    @Override
    public void doCommand(CommandEvent event) {
        String arg = event.getArgs();
        arg = arg.replaceAll("```js", "");
        arg = arg.replaceAll("```", "");
        if (StringUtils.isBlank(arg)) {
            event.replyInDM(event.getClient().getError() + " please provide something to evaluate");
            return;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("Nashorn");
        scriptEngine.put("event", event);
        scriptEngine.put("jda", event.getJDA());
        scriptEngine.put("guild", event.getGuild());
        scriptEngine.put("channel", event.getTextChannel());
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getColor());
        try {
            eb.addField("Result:", "" + scriptEngine.eval(arg.trim()), false);
            event.reply(eb.build());
        } catch (Exception e1) {
            eb.addField("Error:" + e1.getClass().getSimpleName(), e1.getCause().toString(), false);
            Logger.err("Eval command execution Error:" + e1.getClass().getSimpleName() + e1.getCause().toString());
            event.reply(eb.build());
        }
    }
}
