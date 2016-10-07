package timedpermissions.subscription;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.configuration.ConfigurationSection;

public abstract class Subscription {

	private static AtomicInteger nextTempUID = new AtomicInteger(1);

	private String tempUid = String.valueOf(nextTempUID.getAndIncrement());

	public String getTempUID() {
		return tempUid;
	}

	private long expireTime;
	private UUID playerUUID;

	public Subscription(long expireTime, UUID playerUUID) {
		this.expireTime = expireTime;
		this.playerUUID = playerUUID;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > expireTime;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void modifyExpireTime(long mod) {
		expireTime += mod;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public abstract String validate();

	public abstract void apply();

	public abstract void expire();

	public abstract String getInfoString();

	protected Subscription() {
	}

	private static final String CFG_STR_EXPIRE_TIME = "expiretime";
	private static final String CFG_STR_PLAYER_UUID = "playeruuid";

	void save(ConfigurationSection section) {
		section.set(CFG_STR_EXPIRE_TIME, getExpireTime());
		section.set(CFG_STR_PLAYER_UUID, getPlayerUUID().toString());
		saveData(section);
	}

	void load(ConfigurationSection section) {
		expireTime = section.getLong(CFG_STR_EXPIRE_TIME);
		playerUUID = UUID.fromString(section.getString(CFG_STR_PLAYER_UUID));
		loadData(section);
	}

	protected abstract void saveData(ConfigurationSection section);

	protected abstract void loadData(ConfigurationSection section);

	private static final HashMap<String, Class<? extends Subscription>> typeToClass = new HashMap<>();
	private static final HashMap<Class<? extends Subscription>, String> classToType = new HashMap<>();

	static {
		register("maingroup", SubscriptionMainGroup.class);
		register("subgroup", SubscriptionSubGroup.class);
		register("permission", SubscriptionPermission.class);
	}

	public static void register(String type, Class<? extends Subscription> clazz) {
		typeToClass.put(type, clazz);
		classToType.put(clazz, type);
	}

	protected static String getType(Class<? extends Subscription> clazz) {
		return classToType.get(clazz);
	}

	@SuppressWarnings("unchecked")
	protected static <T extends Subscription> T create(String type) {
		try {
			return (T) typeToClass.get(type).newInstance();
		} catch (InstantiationException | IllegalAccessException | NullPointerException e) {
			throw new RuntimeException("Unable to create object for type "+ type, e);
		}
	}

}
