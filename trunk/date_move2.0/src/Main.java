

import core.ExecuteMove;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
//        ExecuteMove em = new ExecuteMove("config/test.xml","database.xml","util.xml");
        ExecuteMove em = new ExecuteMove("config/application.xml");
        em.execute();
        Date date = new Date(System.currentTimeMillis() - time);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        System.out.println("总耗时: " + calendar.get(Calendar.MINUTE) + "分 " + calendar.get(Calendar.SECOND) + "秒");
    }
}