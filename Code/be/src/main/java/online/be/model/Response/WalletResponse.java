package online.be.model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.be.entity.Wallet;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse extends Wallet {
    float balance;
}
