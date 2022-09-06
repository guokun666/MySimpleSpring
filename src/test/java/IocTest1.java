import com.kun.config.KunAppConfig;
import com.kun.service.GuoService;
import com.kun.service.KunService;
import com.spring.KunApplicationContext;

/**
 * @author guokun
 * @date 2022/9/5 21:37
 */
public class IocTest1 {
    public static void main(String[] args) {
        KunApplicationContext kunApplicationContext = new KunApplicationContext(KunAppConfig.class);
        GuoService guoService = (GuoService) kunApplicationContext.getBean("guoService");
        System.out.println(guoService.getKunService());
    }
}
