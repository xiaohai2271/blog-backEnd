package cn.celess.blog.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author : xiaohai
 * @date : 2019/03/28 17:22
 */
public class DateFormatUtil {
    public static String get(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getForXmlDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
        GregorianCalendar gc = new GregorianCalendar();
        String dateString = sdf.format(date);
        try {
            gc.setTime(sdf.parse(dateString));
            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            return date2.toString();
        } catch (DatatypeConfigurationException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getNow() {
        return get(new Date());
    }
}
