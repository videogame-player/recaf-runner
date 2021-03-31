import me.coley.recaf.Recaf;
import me.coley.recaf.plugin.PluginsManager;
import me.coley.recaf.plugin.api.BasePlugin;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecafStart {
    public static void main(String[] args) {
        try {
            String mainClass = null;
            List<String> recafArgs = new ArrayList<>(Arrays.asList(args));
            for (int i = 0; i < args.length; i++) {
                String argument = args[i];
                if (argument.equals("--mainClass")) {
                    mainClass = args[i + 1];
                    for (int i1 = 0; i1 < 2; i1++) {
                        recafArgs.remove(i);
                    }
                    break;
                }
            }
            if (mainClass == null) {
                throw new IllegalStateException("You must specify a plugin main class");
            }

            Class<?> clazz = Class.forName(mainClass);
            System.out.println("Attempting to load " + mainClass + "!");

            try {
                Class<? extends BasePlugin> plugin = clazz.asSubclass(BasePlugin.class);
                PluginsManager.getInstance().addPlugin(plugin.newInstance());
                if (recafArgs.size() > 0) {
                    System.out.println("Added plugin! Launching Recaf with the additional arguments " + String.join(", ", recafArgs));
                } else {
                    System.out.println("Added plugin! Launching Recaf...");
                }
                Recaf.main(recafArgs.toArray(new String[0]));
            } catch (Throwable t) {
                if (t instanceof ClassCastException) {
                    throw new IllegalStateException("FAILED! Please verify your plugin implements BasePlugin");
                } else {
                    t.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}