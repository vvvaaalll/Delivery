package vloboda.deliveryapp.delivery;

public class Order {

    String name;
    String phone;
    String address;
    String note;

    long time;

    public Order(){

    }

    public Order(String name, String phone, String address, String note, long time) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
