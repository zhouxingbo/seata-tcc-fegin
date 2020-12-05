package com.alibaba.cloud.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.sql.SQLException;

/**
 * Created by Administrator on 2018\5\17 0017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class TestCommon {

    @Autowired
    private TransferService transferService;

    /**
     * 转出账户数据 DAO
     */
    @Autowired
    private AccountDAO fromAccountDAO;

    /**
     * 转入账户数据 DAO
     */
    @Autowired
    private AccountDAO toAccountDAO;

    @Test
    public void doTransferSuccess() throws SQLException {
        double initAmount = 100;
        double transferAmount = 4;
        //执行转账操作
        doTransfer("A", "C", transferAmount);

        //校验A账户余额：initAmount - transferAmount
        checkAmount(fromAccountDAO, "A", initAmount - transferAmount);

        //校验C账户余额：initAmount + transferAmount
        checkAmount(toAccountDAO, "C", initAmount + transferAmount);
    }

    /**
     * 执行转账 失败 demo， 'B' 向未知用户 'XXX' 转账，转账失败分布式事务回滚
     */
    @Test
    public void doTransferFailed() throws SQLException {
        double initAmount = 100;
        double transferAmount = 4;
        // 'B' 向未知用户 'XXX' 转账，转账失败分布式事务回滚
        try{
            doTransfer("B", "XXX", transferAmount);
        }catch (Throwable t){
            System.out.println("从账户B向未知账号XXX转账失败.");
        }
        //校验A2账户余额：initAmount
        checkAmount(fromAccountDAO, "B", initAmount);
    }

    /**
     * 执行转账 操作
     * @param transferAmount 转账金额
     */
    public boolean doTransfer(String from, String to, double transferAmount) {
        //转账操作
        boolean ret = transferService.transfer(from, to, transferAmount);
        if(ret){
            System.out.println("从账户"+from+"向"+to+"转账 "+transferAmount+"元 成功.");
            System.out.println();
        }else {
            System.out.println("从账户"+from+"向"+to+"转账 "+transferAmount+"元 失败.");
            System.out.println();
        }
        return ret;
    }

    public static void checkAmount(AccountDAO accountDAO, String accountNo, double expectedAmount) throws SQLException {
        try {
            Account account = accountDAO.getAccount(accountNo);
            Assert.isTrue(account != null, "账户不存在");
            double amount = account.getAmount();
            double freezedAmount = account.getFreezedAmount();
            Assert.isTrue(expectedAmount == amount, "账户余额校验失败");
            Assert.isTrue(freezedAmount == 0, "账户冻结余额校验失败");
        }catch (Throwable t){
            t.printStackTrace();
        }
    }


}

