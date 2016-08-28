package us.codecraft.webmagic.scheduler;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;

/**
 * Use Redis as url scheduler for distributed crawlers.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
public class RedisScheduler implements Scheduler {

    private JedisPool pool;

    private static final String QUEUE_PREFIX = "queue_";

    private static final String SET_PREFIX = "set_";

    private static final String ITEM_PREFIX = "item_";

    public RedisScheduler(String host) {
        pool = new JedisPool(new JedisPoolConfig(), host);
    }

    public RedisScheduler(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public synchronized void push(Request request, Task task) {
        Jedis jedis = pool.getResource();
        try {
            // if cycleRetriedTimes is set, allow duplicated.
            Object cycleRetriedTimes = request.getExtra(Request.CYCLE_TRIED_TIMES);
            // use set to remove duplicate url
            if (cycleRetriedTimes != null || !jedis.sismember(SET_PREFIX + task.getUUID(), request.getUrl())) {
                // use list to store queue
                jedis.rpush(QUEUE_PREFIX + task.getUUID(), request.getUrl());
                jedis.sadd(SET_PREFIX + task.getUUID(), request.getUrl());
                if (request.getExtras() != null) {
                    String field = DigestUtils.shaHex(request.getUrl());
                    String value = JSON.toJSONString(request);
                    jedis.hset((ITEM_PREFIX + task.getUUID()), field, value);
                }
            }
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public synchronized Request poll(Task task) {
        Jedis jedis = pool.getResource();
        try {
            String url = jedis.lpop(QUEUE_PREFIX + task.getUUID());
            if (url == null) {
                return null;
            }
            String key = ITEM_PREFIX + task.getUUID();
            String field = DigestUtils.shaHex(url);
            byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
            if (bytes != null) {
                Request o = JSON.parseObject(new String(bytes), Request.class);
                return o;
            }
            Request request = new Request(url);
            return request;
        } finally {
            pool.returnResource(jedis);
        }
    }
}
