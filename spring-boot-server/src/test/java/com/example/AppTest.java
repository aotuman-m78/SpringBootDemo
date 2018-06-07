package com.example;

import static org.junit.Assert.assertTrue;

import com.dyuproject.protostuff.ProtostuffException;
import com.example.model.Children;
import com.example.model.Parent;
import com.example.serialization.impl.protostuff.ProtostuffSerialization;
import org.junit.Assert;
import org.junit.Test;

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
        Parent parent = new Parent();
        parent.setName("爸爸");
        parent.setAge(28);
        Children c = new Children();
        c.setName("女儿");
        c.setAge(15);
        parent.getChildren().add(c);
        byte[] b = ProtostuffSerialization.serialize(parent);
        Assert.assertNotNull(b);
        Parent p = ProtostuffSerialization.deserialize(b, Parent.class);
        Assert.assertNotNull(p);
        Assert.assertEquals("爸爸", p.getName());
        Assert.assertEquals(Integer.valueOf(28), p.getAge());

//        Assert.assertEquals(1, p.getChildren().size());
//        Children c1 = p.getChildren().get(0);
//        Assert.assertEquals("女儿", c1.getName());
//        Assert.assertEquals(Integer.valueOf(15), c1.getAge());
    }
}
