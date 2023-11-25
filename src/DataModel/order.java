package DataModel;

import java.util.Date;

public class order {
    protected int orders_id;
    protected Date check_in_date;
    protected Date check_out_date;
    protected Date booking_date;
    protected int total_price;
    protected PaymentMethodType payment_method;
    protected OrderStatusType order_status;
    protected int room_id;
    protected int users_id;
    protected int admin_id;

    // Constructor
    public order(int orders_id, Date check_in_date, Date check_out_date, Date booking_date,
            int total_price, PaymentMethodType payment_method, OrderStatusType order_status,
            int room_id, int users_id, int admin_id) {
        this.orders_id = orders_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.booking_date = booking_date;
        this.total_price = total_price;
        this.payment_method = payment_method;
        this.order_status = order_status;
        this.room_id = room_id;
        this.users_id = users_id;
    }

    // Getter and Setter methods for all attributes
    public int getOrders_id() {
        return orders_id;
    }

    public void setOrders_id(int orders_id) {
        this.orders_id = orders_id;
    }

    public Date getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(Date check_in_date) {
        this.check_in_date = check_in_date;
    }

    public Date getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(Date check_out_date) {
        this.check_out_date = check_out_date;
    }

    public Date getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(Date booking_date) {
        this.booking_date = booking_date;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public PaymentMethodType getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentMethodType payment_method) {
        this.payment_method = payment_method;
    }

    public OrderStatusType getOrder_status() {
        return order_status;
    }

    public void setOrder_status(OrderStatusType order_status) {
        this.order_status = order_status;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public enum PaymentMethodType {
        BCA,
        BRI,
        Cash,
        Paylater
    }

    public enum OrderStatusType {
        In_Progress,
        Successful,
        Cancelled
    }
}
