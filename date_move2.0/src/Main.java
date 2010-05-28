

import core.ExecuteMove;

public class Main {
    public static void main(String[] args) {
//        ExecuteMove em = new ExecuteMove("config/application.xml","config/database.xml","config/saver.xml","config/util.xml");
//        ExecuteMove em = new ExecuteMove("config/jiahua.xml","config/database.xml","config/saver.xml","config/util.xml");
//        ExecuteMove em = new ExecuteMove("config/classic.xml","config/database.xml","config/saver.xml","config/util.xml");
//        ExecuteMove em = new ExecuteMove("config/mg.xml","config/database.xml","config/saver.xml","config/util.xml");
//        ExecuteMove em = new ExecuteMove("config/zhonghuitonghe.xml","config/database.xml","config/saver.xml","config/util.xml");
//        ExecuteMove em = new ExecuteMove("config/polystar.xml","config/database.xml","config/saver.xml","config/util.xml");
        ExecuteMove em = new ExecuteMove("config/honda.xml","config/database.xml","config/saver.xml","config/util.xml");
        em.execute();
    }
}