package warmingUp.antifragile.post.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnManyDto <T>{
    private ArrayList<T> data;
    private String message;
}