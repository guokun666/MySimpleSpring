import com.kun.config.KunAppConfig;
import com.kun.service.KunService;
import com.spring.KunApplicationContext;

/**
 * @author guokun
 * @date 2022/9/6 22:46
 */
public class BeanNameAwareTest {
    public static void main(String[] args) {
        KunApplicationContext context = new KunApplicationContext(KunAppConfig.class);
        KunService kunService = (KunService) context.getBean("kunService");
        System.out.println("beanName:\t" + kunService.getBeanName());
    }
}
