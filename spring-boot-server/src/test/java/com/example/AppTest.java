package com.example;

import static org.junit.Assert.assertTrue;

import com.example.model.Children;
import com.example.model.Parent;
import com.example.serialization.impl.hessian.HessianSerializer;
import com.example.serialization.impl.kyro.KryoSerializer;
import com.example.serialization.impl.protostuff.ProtostuffSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testSerialization() throws InstantiationException, IllegalAccessException {
        List<Parent> list = new ArrayList();
        Parent parent = new Parent();
        parent.setName("爸爸");
        parent.setAge(28);
        Children c = new Children();
        c.setName("女儿");
        c.setAge(15);
        parent.getChildren().add(c);
        list.add(parent);
        byte[] b = HessianSerializer.serialize(list);
        Assert.assertNotNull(b);
        List<Parent> lists = HessianSerializer.deserialize(b);
        Parent p = lists.get(0);
        Assert.assertNotNull(p);
        Assert.assertEquals("爸爸", p.getName());
        Assert.assertEquals(Integer.valueOf(28), p.getAge());

        Assert.assertEquals(1, p.getChildren().size());
        Children c1 = p.getChildren().get(0);
        Assert.assertEquals("女儿", c1.getName());
        Assert.assertEquals(Integer.valueOf(15), c1.getAge());
    }
}
