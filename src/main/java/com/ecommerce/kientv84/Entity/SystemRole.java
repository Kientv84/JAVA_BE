package com.ecommerce.kientv84.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "System_Role")
public class SystemRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "System_Role_Name", nullable = false)
    private String systemRoleName;

    @Column(name= "System_Role_Description")
    private String systemRoleDescription;

    @Column(name = "Status")
    private String status;

    @Column(name = "CreatedBy", length = 500)
    private String createBy;

    @Column(name = "CreatedDate")
    @Temporal(TemporalType.TIMESTAMP) // Đánh dấu anotation temporal ánh xạ dữ liệu kiểu date (dd/mm/yy)
    private Date createDate;

    @Column(name = "UpdatedBy", length = 500)
    private String updateBy;

    @Column(name = "UpdatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @OneToMany(mappedBy = "systemRole", cascade = CascadeType.ALL)  //ascade = CascadeType.ALL cho phép thao tác trên SystemRole sẽ tự lan sang SystemUser
    @JsonManagedReference //là phần "được giữ lại".
    //, dùng để xử lý quan hệ hai chiều (bi-directional) trong JSON serialization bằng thư viện Jackson (dùng trong Spring Boot).

    // @JsonIgnore: sd để thư viện serialize JSON mặc định trong Spring Boot) không serialize trường users trong SystemRole.
    private List<SystemUser> users;
}

// 7/5 have bugs:
//Lý do bạn gặp tình trạng JSON bị lặp vô hạn (infinite recursion) trong Postman là do
// quan hệ hai chiều (bidirectional relationship) giữa các entity JPA của bạn — cụ thể là giữa SystemUser và SystemRole.
//@OneToMany(mappedBy = "systemRole")
//private List<SystemUser> users;
//Vì vậy khi bạn trả về một SystemUser, nó sẽ serialize toàn bộ SystemRole kèm theo,
// nhưng SystemRole lại có danh sách users, trong đó lại có SystemUser, rồi lại SystemRole, rồi lại users, cứ thế lặp vô hạn.

//@JsonBackendReference/ @JsonManagedReference dùng để tránh việc lập Json vô hạn (Infinite recursion) (Lập lại đệ quy)
// ==> Chỉ ảnh hưởng khi serialize thành JSON, không ảnh hưởng đến việc truy cập bằng code Java.