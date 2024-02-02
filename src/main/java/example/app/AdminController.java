package example.app;

import example.domain.admin.AdminService;
import example.entity.contorller.AddUser;
import example.entity.contorller.MockUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import scala.util.Random$;

@RestController
public class AdminController {

  @Autowired
  private AdminService adminService;

  @GetMapping("/")
  public MockUser mock() {
    return MockUser.apply(Random$.MODULE$.nextString(10));
  }

  @GetMapping("/list")
  public List<MockUser> mockList() {
    ArrayList<MockUser> users = new ArrayList<>();
    users.add(MockUser.apply(Random$.MODULE$.nextString(10)));
    return users;
  }


  @PostMapping("/hello")
  public String admin(@RequestBody AddUser addUser) {
    return adminService.add(addUser).toString();
  }

  @GetMapping("/admin")
  public Response<List<MockUser>> hello() {
    ArrayList<MockUser> objects = new ArrayList<>();
    objects.add(MockUser.apply("1"));
    return new Response<>(objects);
  }
}
