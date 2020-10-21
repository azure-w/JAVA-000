### Week_01 课后作业
#### Q2:自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。
```java
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wan.chengfei
 * @create 2020-10-20 21:27:41
 */
public class OzClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = resolveSourceFile();
        return defineClass(name, bytes, 0, bytes.length);
    }
    
    private byte[] resolveSourceFile() {
        byte[] source = new byte[0];
        try {
            source = IOUtils.toByteArray(this.getClass().getResourceAsStream("/Hello.xlass"));
            for (int i = 0; i < source.length; i++) {
                source[i] = (byte) (255 - source[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source;
    }

    public static void main(String[] args) {
        try {
            Class<?> clazz = new OzClassLoader().findClass("Hello");
            Method method = clazz.getMethod("hello");
            method.invoke(clazz.newInstance());
        } catch (ClassNotFoundException | IllegalAccessException
                | InvocationTargetException | InstantiationException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
```
![avatar](/Week_01/image/Week01-Q2.png)

### Q3:画一张图，展示 Xmx、Xms、Xmn、Meta、DirectMemory、Xss 这些内存参数的关系。

![avatar](/Week_01/image/Week01-Q3.png)