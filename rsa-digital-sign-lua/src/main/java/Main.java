import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.ast.Chunk;
import org.luaj.vm2.ast.Exp;
import org.luaj.vm2.ast.Visitor;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.parser.LuaParser;
import org.luaj.vm2.parser.ParseException;

import javax.script.Compilable;
import javax.script.CompiledScript;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String args[]) throws NoSuchAlgorithmException {
        String script = "rsatest.lua";
        Globals globals = JsePlatform.standardGlobals();

        // Use the convenience function on Globals to load a chunk.
        LuaValue chunk = globals.loadfile(script);

        // Use any of the "call()" or "invoke()" functions directly on the chunk.
        chunk.call(LuaValue.valueOf(script));
//        System.out.println(chunk.checkint());
    }
}
