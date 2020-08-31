package map;

import java.math.BigDecimal;

/**
 * @author linjing
 * @date: Created in 2020/8/21
 */
public class TaatCollectDataSetupEntity {
    /** 数据采集id */
    private int id;
    /** 数据变动原因 */
    private String createReason;
    /** 数据采集日期 */
    private String createDate;
    /** 数据采集时间 */
    private String createTime;
    /** 数据采集人 */
    private String createUser;
    /** 基金代码 */
    private String fundCode;
    /** 基金名称 */
    private String fundName;
    /** 主基金代码 */
    private String mainFundCode;
    /** 确认日期 */
    private String cdate;
    /** 销售商代码 */
    private String agencyNo;
    /** 销售商名称 */
    @SourceField(name="销售商")
    private String agencyName;
    /** 托管行编号 */
    private String trusteeCode;
    @SourceField(name="GRQRJE")
    /** 个人确认金额 */
    private BigDecimal individualBalance;
    /** 机构确认金额 */
    private BigDecimal institutionBalance;
    /** 总金额 */
    private BigDecimal totalBalance;
    /** 个人确认份额 */
    private BigDecimal individualShares;
    /** 机构确认份额 */
    private BigDecimal institutionShares;
    /** 总份额 */
    private BigDecimal totalShares;
    /** 手续费 */
    private BigDecimal tradeFare;
    /** 未确认金额 */
    private BigDecimal unConfirmBalance;
    /** 利息转份额 */
    private BigDecimal interest2Share;
    /** 利息归基金资产 */
    private BigDecimal interest2Fund;
    /** 总人数 */
    private BigDecimal investorAmount;
    /** TA确认状态 */
    private String confirmStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateReason() {
        return createReason;
    }

    public void setCreateReason(String createReason) {
        this.createReason = createReason;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getMainFundCode() {
        return mainFundCode;
    }

    public void setMainFundCode(String mainFundCode) {
        this.mainFundCode = mainFundCode;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getAgencyNo() {
        return agencyNo;
    }

    public void setAgencyNo(String agencyNo) {
        this.agencyNo = agencyNo;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getTrusteeCode() {
        return trusteeCode;
    }

    public void setTrusteeCode(String trusteeCode) {
        this.trusteeCode = trusteeCode;
    }

    public BigDecimal getIndividualBalance() {
        return individualBalance;
    }

    public void setIndividualBalance(BigDecimal individualBalance) {
        this.individualBalance = individualBalance;
    }

    public BigDecimal getInstitutionBalance() {
        return institutionBalance;
    }

    public void setInstitutionBalance(BigDecimal institutionBalance) {
        this.institutionBalance = institutionBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getIndividualShares() {
        return individualShares;
    }

    public void setIndividualShares(BigDecimal individualShares) {
        this.individualShares = individualShares;
    }

    public BigDecimal getInstitutionShares() {
        return institutionShares;
    }

    public void setInstitutionShares(BigDecimal institutionShares) {
        this.institutionShares = institutionShares;
    }

    public BigDecimal getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(BigDecimal totalShares) {
        this.totalShares = totalShares;
    }

    public BigDecimal getTradeFare() {
        return tradeFare;
    }

    public void setTradeFare(BigDecimal tradeFare) {
        this.tradeFare = tradeFare;
    }

    public BigDecimal getUnConfirmBalance() {
        return unConfirmBalance;
    }

    public void setUnConfirmBalance(BigDecimal unConfirmBalance) {
        this.unConfirmBalance = unConfirmBalance;
    }

    public BigDecimal getInterest2Share() {
        return interest2Share;
    }

    public void setInterest2Share(BigDecimal interest2Share) {
        this.interest2Share = interest2Share;
    }

    public BigDecimal getInterest2Fund() {
        return interest2Fund;
    }

    public void setInterest2Fund(BigDecimal interest2Fund) {
        this.interest2Fund = interest2Fund;
    }

    public BigDecimal getInvestorAmount() {
        return investorAmount;
    }

    public void setInvestorAmount(BigDecimal investorAmount) {
        this.investorAmount = investorAmount;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }
}
