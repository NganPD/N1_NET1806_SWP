
package online.be.model.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WithDrawRequest {
    String accountNumber;
    String accountName;
    String bankName;
    float amount;
}
