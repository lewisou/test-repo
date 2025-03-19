package co.nz.tsb.interview.bankrecmatchmaker;

public class MatchItem {

    private final String paidTo;
    private final String transactionDate;
    private final float total;
    private final String docType;


    public MatchItem(String paidTo, String transactionDate, float total, String docType) {
        this.paidTo = paidTo;
        this.transactionDate = transactionDate;
        this.total = total;
        this.docType = docType;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public float getTotal() {
        return total;
    }

    public String getDocType() {
        return docType;
    }

}
