package fudan.mmdb.mds.web.repository.mongo;

import org.springframework.data.repository.CrudRepository;

import fudan.mmdb.mds.core.model.MdsDocument;

public interface  MongoDocRepository extends CrudRepository<MdsDocument, String>{
 
}
