package target.myretail.api.dao;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import target.myretail.api.model.ProductTable;

public interface ProductDao extends ReactiveCassandraRepository<ProductTable, Long> {
}
