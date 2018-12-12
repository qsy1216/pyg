import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath*:spring/applicationContext-solr.xml",
                "classpath:spring/applicationContext-dao.xml"})
public class TestSearch {
    @Autowired
    private SolrTemplate sorlTemplate;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbItemMapper itemMapper;



}
