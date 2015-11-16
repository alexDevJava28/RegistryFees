/**
 * Created by khodackovskiy on 16.11.2015.
 */
public class Line {

    private String companyName;
    private long sumToPay;
    private String whatWePayFor;

    public Line (String companyName, long sumToPay, String whatWePayFor){

        this.companyName = companyName;
        this.sumToPay = sumToPay;
        this.whatWePayFor = whatWePayFor;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getSumToPay() {
        return sumToPay;
    }

    public void setSumToPay(long sumToPay) {
        this.sumToPay = sumToPay;
    }

    public String getWhatWePayFor() {
        return whatWePayFor;
    }

    public void setWhatWePayFor(String whatWePayFor) {
        this.whatWePayFor = whatWePayFor;
    }
}
