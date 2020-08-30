package sql.spring.jdbctemplate;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author linjing
 * @date: Created in 2020/7/7
 */
/**
 * create table gjdftreportrecord_test (
 *     vc_gtlkmcs  varchar2(2),
 *     EN_DFFAZCLHJ    number(16,2) ,
 *     EN_DFFAZCJZ    number(16,2) ,
 *     EN_YHCK    number(16,2) ,
 *     EN_GQTZ    number(16,2) ,
 *     EN_QTGQTZ    number(16,2) ,
 *     EN_DXZFTZ    number(16,2) ,
 *     EN_XSBTZ    number(16,2) ,
 *     EN_JNZQ_JSBFJ    number(16,2) ,
 *     EN_JNZQ_CCBZJ    number(16,2) ,
 *     EN_JNZQ_GPTZ    number(16,2) ,
 *     EN_JNZQ_ZQTZ    number(16,2) ,
 *     EN_JNZQ_ZCZCZQ    number(16,2) ,
 *     EN_JNZQ_JJTZ    number(16,2) ,
 *     EN_JNZQ_QTZQTZ    number(16,2) ,
 *     EN_MRFSJRZC    number(16,2) ,
 *     EN_LCCP_YH    number(16,2) ,
 *     EN_LCCP_XT    number(16,2) ,
 *     EN_LCCP_JJ    number(16,2) ,
 *     EN_LCCP_BX    number(16,2) ,
 *     EN_LCCP_ZQ    number(16,2) ,
 *     EN_LCCP_QH    number(16,2) ,
 *     EN_LCCP_SM    number(16,2) ,
 *     EN_LCCP_WBA    number(16,2) ,
 *     EN_LLTZ    number(16,2) ,
 *     EN_ZQ_YHWD    number(16,2) ,
 *     EN_ZQ_XTDK    number(16,2) ,
 *     EN_ZQ_YSZK    number(16,2) ,
 *     EN_ZQ_SYQ    number(16,2) ,
 *     EN_ZQ_PJ    number(16,2) ,
 *     EN_ZQ_QT    number(16,2) ,
 *     EN_JWTZ    number(16,2) ,
 *     EN_QTZCTZ   varchar2(255) ,
 *     EN_QLF  number(16,2)
 * );
 * insert into gjdftreportrecord_test (vc_gtlkmcs,EN_DFFAZCLHJ, EN_DFFAZCJZ, EN_YHCK, EN_GQTZ, EN_QTGQTZ, EN_DXZFTZ, EN_XSBTZ, EN_JNZQ_JSBFJ, EN_JNZQ_CCBZJ, EN_JNZQ_GPTZ, EN_JNZQ_ZQTZ, EN_JNZQ_ZCZCZQ, EN_JNZQ_JJTZ, EN_JNZQ_QTZQTZ, EN_MRFSJRZC, EN_LCCP_YH, EN_LCCP_XT, EN_LCCP_JJ, EN_LCCP_BX, EN_LCCP_ZQ, EN_LCCP_QH, EN_LCCP_SM, EN_LCCP_WBA, EN_LLTZ, EN_ZQ_YHWD, EN_ZQ_XTDK, EN_ZQ_YSZK, EN_ZQ_SYQ, EN_ZQ_PJ, EN_ZQ_QT, EN_JWTZ, EN_QTZCTZ, EN_QLF)
 * values ('1',18227708.34, 18208553.57, '42338.74', '0.00', '', '0.00', '0.00', '6227515.50', '2303688.00', '4921350.00', '0.00', '0.00', '4014209.10', '', '700007.00', '', '', '', '', '', '', '', '', '0.00', '', '', '', '', '', '', '0', '', 18600.00);
 */

/**
 * select distinct   vc_gtlkmcs, decode(jz1.vc_kmdm, '资产类合计:',jz1.en_sz, 0)  en_dffazclhj
 *                                       , decode(jz2.vc_kmdm, '基金资产净值:', jz2.en_sz, 0) en_dffazcjz
 *                                       ,en_yhck, en_gqtz, en_qtgqtz, en_dxzftz, en_xsbtz
 *                                       , en_jnzq_jsbfj, en_jnzq_ccbzj, en_jnzq_gptz
 *                                       , en_jnzq_zqtz, en_jnzq_zczczq, en_jnzq_jjtz
 *                                       , en_jnzq_qtzqtz, en_mrfsjrzc
 *                                       , en_lccp_yh, en_lccp_xt, en_lccp_jj, en_lccp_bx
 *                                       , en_lccp_zq, en_lccp_qh, en_lccp_sm, en_lccp_wba
 *                                       , en_lltz, en_zq_yhwd, en_zq_xtdk, en_zq_yszk
 *                                       , en_zq_syq, en_zq_pj, en_zq_qt, en_jwtz, en_qtzctz, en_qlf
 *                                          from ambers.gjdftreportrecord gt
 *                                             , ambers.gjdf_rptxx gr
 *                                              ,ambers.gjdf_cpyxjcb gc
 *                                             , ambers.GJDFTFUNDOPERATIONALDATA ga
 *                                             , ambers.gjdf_qlfje gq
 *                                             , hsfa.TTMP_H_GZB@dfgz_hsfa jz1
 *                                             , hsfa.TTMP_H_GZB@dfgz_hsfa jz2
 *                                             , TPB_FUNDINFOATTACH fi
 *                                             , (select * from TDICTIONARY where L_KEYNO = '660173' and c_keyvalue <> '#') d1
 *                                          where  gt.n_value_year_def   =  to_char(gr.d_date, 'yyyy')
 *                                             and gt.n_value_batch_def  =  gr.l_season
 *                                             and gt.tbi_c_id  = gr.vc_code
 *                                             and gt.c_Status = '0'
 *                                             and gr.l_id  =  gc.l_id
 *                                             and gt.l_id = ga.rr_l_id(+)
 *                                             and gc.l_ztbh = jz1.L_ZTBH(+)
 *                                             and to_date(gc.D_CURRENT,'yyyy-mm-dd') = jz1.D_YWRQ(+)
 *                                             and jz1.VC_KMDM = ('资产类合计:')
 *                                             and gc.l_ztbh = jz2.L_ZTBH(+)
 *                                             and to_date(gc.D_CURRENT,'yyyy-mm-dd') = jz2.D_YWRQ(+)
 *                                             and jz2.VC_KMDM = ( '基金资产净值:')
 *                                             and gc.vc_code = gq.vc_code(+)
 *                                             and gc.d_current = gq.d_date(+)
 *                                             and gc.vc_code = fi.C_FUNDCODE(+)
 *                                             and FI.C_ORGANIZATIONFORM = D1.c_keyvalue(+)
 *                                             and gt.tbi_c_id = 'NB0088'
 *                                             and gt.n_value_year_def = '2020'
 *                                             and gt.n_value_batch_def = '2';
 */
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
@Slf4j
public class TestAmbersAssetCalProcedure {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void test(){
        /**
         * 参与‘内部校验逻辑’的字段
         * 算法一：当估值系统参数“共同类科目余额归入资产”选择“1 直接归入资产（无论正负）”，表内期末总资产=从现金类资产到其他资产的各项投资金额（除“期货、期权及其他衍生品投资”项外）
         * 算法二：当估值系统参数“共同类科目余额归入资产”选择“2 按共同类二级科目余额规则（正-资产，负-负债）”，表内期末总资产=从现金类资产到其他资产的各项投资金额（除“期货、期权及其他衍生品投资”项外）+个股期权、商品期货期权中的权利方；
         */
        String[] method1AssetFied = {"en_yhck", "en_gqtz", "en_qtgqtz", "en_dxzftz", "en_xsbtz"
                , "en_jnzq_jsbfj", "en_jnzq_ccbzj", "en_jnzq_gptz"
                , "en_jnzq_zqtz", "en_jnzq_zczczq", "en_jnzq_jjtz"
                , "en_jnzq_qtzqtz", "en_mrfsjrzc"
                , "en_lccp_yh", "en_lccp_xt", "en_lccp_jj", "en_lccp_bx"
                , "en_lccp_zq", "en_lccp_qh", "en_lccp_sm", "en_lccp_wba"
                , "en_lltz", "en_zq_yhwd", "en_zq_xtdk", "en_zq_yszk"
                , "en_zq_syq", "en_zq_pj", "en_zq_qt", "en_jwtz", "en_qtzctz"};

        String[] method2AssetFied = {"en_yhck", "en_gqtz", "en_qtgqtz", "en_dxzftz", "en_xsbtz"
                , "en_jnzq_jsbfj", "en_jnzq_ccbzj", "en_jnzq_gptz"
                , "en_jnzq_zqtz", "en_jnzq_zczczq", "en_jnzq_jjtz"
                , "en_jnzq_qtzqtz", "en_mrfsjrzc"
                , "en_lccp_yh", "en_lccp_xt", "en_lccp_jj", "en_lccp_bx"
                , "en_lccp_zq", "en_lccp_qh", "en_lccp_sm", "en_lccp_wba"
                , "en_lltz", "en_zq_yhwd", "en_zq_xtdk", "en_zq_yszk"
                , "en_zq_syq", "en_zq_pj", "en_zq_qt", "en_jwtz", "en_qtzctz", "en_qlf"};
        String sql = "select * from gjdftreportrecord_test";
        Map dataset = jdbcTemplate.queryForMap(sql);

        BigDecimal autoCalAsset;
        String gtlkmcs = dataset.get("vc_gtlkmcs").toString();
        if("1".equals(gtlkmcs)){
            autoCalAsset = sumSet(method1AssetFied, dataset, 0);
        }else{
            autoCalAsset = sumSet(method2AssetFied, dataset, 0);
        }
        log.debug("现有表内各项资产之和为{}",autoCalAsset);


        BigDecimal autoCalAsset1,autoCalAsset2;
        autoCalAsset1 =  sumSet(method1AssetFied,dataset,1);
        log.debug("算法一：表内各项资产之和为{}",autoCalAsset1);
        autoCalAsset2 =  sumSet(method2AssetFied,dataset,1);
        log.debug("算法二：表内各项资产之和为{}",autoCalAsset2);
        log.debug("算法一和算法二相差：表内各项资产之和为{}",autoCalAsset2.subtract(autoCalAsset1));

    }
    private BigDecimal sumSet(String[] fieldGroup, Map dataset, int row) {
        BigDecimal sumAsset = new BigDecimal(BigInteger.ZERO);
        for (int n = 0; n < fieldGroup.length; n++) {
            Object value = dataset.get(fieldGroup[n]);
            if(value == null){
                continue;
            }
            String tempValue =value.toString();
            if (tempValue.indexOf("#") > 0) {
                while (tempValue.indexOf("#") > 0) {
                    int startSite = tempValue.indexOf("#");
                    int endSite = tempValue.indexOf("；");
                    // add by  linjing  2019-04-11 如果末尾没有封号，则endSite 取tempValue的长度
                    if(endSite == -1) {
                        endSite=tempValue.length();
                    }
                    String splitMoney = tempValue.substring(startSite + 1, endSite);
                    if (splitMoney.indexOf(",") >= 0 || splitMoney.indexOf("；") > 0) {
                        break;
                    }
                    if (!isNumber(splitMoney)) {
                        break;
                    }
                    sumAsset = sumAsset.add(new BigDecimal(splitMoney));
                    if(endSite == tempValue.length()) {
                        break;
                    }
                    tempValue = tempValue.substring(endSite + 1, tempValue.length());
                }
            } else {
                if (!isNumber(tempValue)) {
                    tempValue = "0";
                }
                sumAsset = sumAsset.add(new BigDecimal(tempValue));
            }
        }
        return sumAsset;
    }


    /**
     * 是否为金额数字
     **/
    public static boolean isNumber(String str) {
        //金额验证
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[+-]?\\d+(\\.\\d*)?$"); // 判断小数点后2位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }
}
