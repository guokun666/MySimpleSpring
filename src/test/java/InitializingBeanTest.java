import com.kun.config.KunAppConfig;
import com.spring.KunApplicationContext;

/**
 * @author guokun
 * @date 2022/9/5 21:37
 */
public class InitializingBeanTest {
    public static void main(String[] args) {
        KunApplicationContext kunApplicationContext = new KunApplicationContext(KunAppConfig.class);
    }
}
