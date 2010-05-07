

import core.ExecuteMove;

public class Main {
    public static void main(String[] args) {
        ExecuteMove em = new ExecuteMove("config/application.xml","config/database.xml","config/saver.xml","config/util.xml");
//        ExecuteMove em = new ExecuteMove("config/jiahua.xml","config/database.xml","config/saver.xml","config/util.xml");
        em.execute();
    }
}