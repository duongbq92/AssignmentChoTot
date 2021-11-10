package commons;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:${environment}.properties"})
public interface Environment extends Config{
	String url();
	
	@Key("db.hostname")
	String databaseHostname();
	
	@Key("db.username")
	String databaseUserName();
	
	@Key("db.password")
	String databasePassWord();

}
