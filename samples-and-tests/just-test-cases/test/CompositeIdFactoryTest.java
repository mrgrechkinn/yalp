import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.CompositeIdEntity;
import models.CompositeIdForeignA;
import models.CompositeIdForeignB;
import models.CompositeIdPk;

import org.junit.Before;
import org.junit.Test;

import yalp.Logger;
import yalp.db.Model;
import yalp.db.Model.Factory;
import yalp.db.Model.Property;
import yalp.db.jpa.JPA;
import yalp.test.Fixtures;
import yalp.test.UnitTest;

public class CompositeIdFactoryTest extends UnitTest {
    
    @Before
    public void setup() {
    	Fixtures.delete(CompositeIdEntity.class, CompositeIdForeignA.class, CompositeIdForeignB.class);
    }

    @Test
    public void testGetId() {
    	Factory factory = Model.Manager.factoryFor(CompositeIdEntity.class);
    	assertNotNull(factory);
    	CompositeIdForeignA a = new CompositeIdForeignA();
    	a.save();
    	CompositeIdForeignB b = new CompositeIdForeignB();
    	b.save();
    	CompositeIdEntity e = new CompositeIdEntity();
    	e.compositeIdForeignA = a;
    	e.compositeIdForeignB = b;
    	e.save();
    	
		// let's get its key
    	Object id = factory.keyValue(e);
    	assertNotNull(id);
    	assertTrue(id instanceof CompositeIdPk);
    	CompositeIdPk pk = (CompositeIdPk) id;
    	assertEquals(a.id, pk.getCompositeIdForeignA());
    	assertEquals(b.id, pk.getCompositeIdForeignB());
    }

    @Test
    public void testBindById() {
    	Factory factory = Model.Manager.factoryFor(CompositeIdEntity.class);
    	assertNotNull(factory);
    	CompositeIdForeignA a = new CompositeIdForeignA();
    	a.save();
    	CompositeIdForeignB b = new CompositeIdForeignB();
    	b.save();
    	CompositeIdEntity e = new CompositeIdEntity();
    	e.compositeIdForeignA = a;
    	e.compositeIdForeignB = b;
    	e.save();

		// let's get its key
    	Object id = factory.keyValue(e);
    	Model eDB = factory.findById(id);
    	assertEquals(e, eDB);
    }


}

