package sql.oracle;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 */

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.junit.Test;
import sql.JDBCUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestOracle {
    /*
     * create or replace procedure queryEmpInfo(eno in number,
                                             pename out varchar2,
                                             psal   out number,
                                             pjob   out varchar2)
     */
    @Test
    public void testProcedure(){
        //{call <procedure-name>[(<arg1>,<arg2>, ...)]}
        String sql = "{call queryEmpInfo(?,?,?,?)}";
        Connection conn = null;
        CallableStatement call = null;
        try {
            conn = JDBCUtils.getConnection();
            call = conn.prepareCall(sql);

            //对于in参数，赋值
            call.setInt(1, 7839);

            //对于out参数，申明
            call.registerOutParameter(2, OracleTypes.VARCHAR);
            call.registerOutParameter(3, OracleTypes.NUMBER);
            call.registerOutParameter(4, OracleTypes.VARCHAR);

            //执行
            call.execute();

            //取出结果
            String name = call.getString(2);
            double sal = call.getDouble(3);
            String job = call.getString(4);
            System.out.println(name+"\t"+sal+"\t"+job);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.release(conn, call, null);
        }
    }

    /*
     * create or replace function queryEmpIncome(eno in number)
    return number
     */
    @Test
    public void testFunction(){
        //{?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
        String sql = "{?=call queryEmpIncome(?)}";

        Connection conn = null;
        CallableStatement call = null;
        try {
            conn = JDBCUtils.getConnection();
            call = conn.prepareCall(sql);

            //对于out参数，申明
            call.registerOutParameter(1, OracleTypes.NUMBER);

            //对于in参数，赋值
            call.setInt(2, 7839);

            //执行
            call.execute();

            //取出结果
            double income = call.getDouble(1);
            System.out.println(income);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.release(conn, call, null);
        }
    }
    /*
    查询某个部门中所有员工的所有信息
    包头
    CREATE OR REPLACE PACKAGE MYPACKAGE AS
      type empcursor is ref cursor;
      procedure queryEmpList(dno in number,empList out empcursor);
    END MYPACKAGE;

    包体
    CREATE OR REPLACE PACKAGE BODY MYPACKAGE AS
      procedure queryEmpList(dno in number,empList out empcursor) AS
      BEGIN
        open empList for select * from emp where deptno=dno;
      END queryEmpList;
    END MYPACKAGE;
    */
    @Test
    public void testCursor(){
//        String sql = "{call MYPACKAGE.queryopenperiodList(?)}";
        String sql = "{call sp_ta_openperiod_checkDiff(?)}";

        Connection conn = null;
        CallableStatement call = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            call = conn.prepareCall(sql);
            call.registerOutParameter(1, OracleTypes.CURSOR);
            call.execute();

            //取出集合
            rs = ((OracleCallableStatement)call).getCursor(1);
            /*while(rs.next()){
                String fundcode = rs.getString("c_fundcode");
                String fundname = rs.getString("c_fundname");
                String begindate = rs.getString("d_begindate");
                String enddate = rs.getString("d_enddate");
                String openstatus = rs.getString("c_openstatus");
                System.out.println(fundcode+"\t"+fundname+"\t"+begindate+"\t"+enddate+"\t"+openstatus);
            }*/
            List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
            if (rs != null) {
                int cl = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Map<String, Object> each = new HashMap<String, Object>();
                    for (int i = 0; i < cl; i++) {
                        String key = rs.getMetaData().getColumnName(i + 1);
                        Object val = rs.getObject(key);
                        each.put(key, val);
                    }
                    rowList.add(each);
                }
                System.out.println(rowList.size());
            } else {
                System.out.println("cursor is null!!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.release(conn, call, rs);
        }
    }
}