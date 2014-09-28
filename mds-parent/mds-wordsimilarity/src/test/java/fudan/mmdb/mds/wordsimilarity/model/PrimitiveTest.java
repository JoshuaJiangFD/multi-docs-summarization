package fudan.mmdb.mds.wordsimilarity.model;


import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class PrimitiveTest {

	
	@Test
	public void test_loadData(){
		Primitive.LoadData();
		Assert.assertTrue(Primitive.ALLPRIMITIVES.size()!=0);
		Assert.assertTrue(Primitive.PRIMITIVESID.size()!=0);
	}
	
    /**
     * test the method {@link Primitive#getParents(String)}.
     */
	@Test
    public void test_getParents(){
        String primitive = "攻打";
        List<Integer> list = Primitive.getParents(primitive);
        Assert.assertTrue(list.size()!=0);
    }

}
