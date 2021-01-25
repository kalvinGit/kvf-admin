package com.kalvin.kvf.common.utils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * redis redisTemplate api工具类
 * Create by Kalvin on 2021/01/21.
 */
@Component
@ConditionalOnProperty(name = "kvf.cache.enable", havingValue = "true")
public class RedisKit {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisKit(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // -------------------key 相关操作---------------------

    /**
     * 删除 key
     *
     * @param key 要删除的 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除 key
     *
     * @param keys 要删除的 键 的集合
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 是否存在 key
     *
     * @param key 判断该 键 是否存在
     * @return 存在返回 true, 不存在返回 false
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key     要设置的 键
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 设置成功返回 true, 设置失败返回 false
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置在什么时间过期
     *
     * @param key  要设置的 键
     * @param date 过期时间
     * @return 设置成功返回 true, 设置失败返回 false
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 查找匹配的 key
     *
     * @param pattern 匹配字符串
     * @return 满足匹配条件的 键 的 Set 集合
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key     要移动的 键
     * @param dbIndex 要移动到的 db 的序号, 从 0 开始
     * @return 移动成功返回 true, 移动失败返回 false
     */
    public Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key 要移除过期时间的 键
     * @return 移除成功返回 true, 并且该 key 将持久存在, 移除失败返回 false
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key  要查询剩余过期时间的 键
     * @param unit 时间的单位
     * @return 剩余的过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 返回 key 的剩余的过期时间, 默认时间单位: 秒
     *
     * @param key 要查询剩余过期时间的 键
     * @return 剩余的过期时间, 单位: 秒
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return 随机获取的 键 的名称
     */
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey 修改前的 键 的名称
     * @param newKey 修改后的 键 的名称
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
     *
     * @param oldKey 修改前的 键 的名称
     * @param newKey 修改后的 键 的名称
     * @return 修改成功返回 true, 修改失败返回 false
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key 要查询类型的 键
     * @return key 的数据类型
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    // -------------------string 相关操作---------------------

    /**
     * 设置指定 key 的值
     *
     * @param key   要设置的 键
     * @param value 要设置的 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key 键 的名称
     * @return 键 对应的 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 返回 key 的 字符串值 中指定位置的 子字符串
     *
     * @param key   要获取值的 键
     * @param start 开始位置, 最小值: 0
     * @param end   结束位置, 最大值: 字符串 - 1, 若为 -1 则是获取整个字符串值
     * @return 指定 键 的 字符串值 的 子字符串
     */
    public String getRange(String key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值( old value )
     *
     * @param key   要设置值的 键
     * @param value 新值
     * @return 旧值
     */
    public Object getAndSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位( bit )
     *
     * @param key    键
     * @param offset 偏移量
     * @return 指定偏移量上的 位( 0 / 1)
     */
    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 批量获取 key 的 值
     *
     * @param keys 要获取值的 键 的集合
     * @return key对应的值的集合
     */
    public List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第 offset 位值变为 value
     *
     * @param key    要设置的 键
     * @param offset 偏移多少位
     * @param value  值, true 为 1,  false 为 0
     * @return 设置成功返回 true, 设置失败返回 false
     */
    public Boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位, 天: TimeUnit.DAYS 小时: TimeUnit.HOURS 分钟: TimeUnit.MINUTES
     *                秒: TimeUnit.SECONDS 毫秒: TimeUnit.MILLISECONDS
     */
    public void setEx(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param key   键
     * @param value 值
     * @return 之前已经存在返回 false, 不存在返回 true
     */
    public Boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
     *
     * @param key    键
     * @param value  值
     * @param offset 从指定位置开始覆写
     */
    public void setRange(String key, Object value, long offset) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    /**
     * 获取字符串的长度
     *
     * @param key 键
     * @return 该 key 对应的 值的长度
     */
    public Long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 批量添加 key-value
     *
     * @param maps key-value 的 map 集合
     */
    public void multiSet(Map<String, Object> maps) {
        redisTemplate.opsForValue().multiSet(maps);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在才会设置成功
     *
     * @param maps key-value 集合
     * @return 之前已经存在返回 false, 不存在返回 true
     */
    public Boolean multiSetIfAbsent(Map<String, Object> maps) {
        return redisTemplate.opsForValue().multiSetIfAbsent(maps);
    }

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key       键
     * @param increment 自增量
     * @return 增加后的值
     */
    public Long incrBy(String key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * 自增长, 增长量为浮点数
     *
     * @param key       键
     * @param increment 自增量
     * @return 增加后的值
     */
    public Double incrByFloat(String key, double increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * 将 value 追加到指定 key 的值的末尾
     *
     * @param key   键
     * @param value 要追加的值
     * @return 追加值后新值的长度
     */
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    // -------------------hash 相关操作-------------------------

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key   键
     * @param field 字段名( 即 map 中的 key )
     * @return 值
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取给定 哈希表 中的所有键值对
     *
     * @param key 哈希表
     * @return 所有的 键值对
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取指定 哈希表 中所有给定字段的值
     *
     * @param key    哈希表
     * @param fields 要获取值的字段集合
     * @return 哈希表中所有给定字段的值
     */
    public List<Object> hMultiGet(String key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }

    /**
     * 向指定 哈希表 中存储一个 键值对
     *
     * @param key     哈希表
     * @param hashKey 字段名
     * @param value   值
     */
    public void hPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 向指定 哈希表 中存储多个 键值对
     *
     * @param key  哈希表
     * @param maps 键值对集合
     */
    public void hPutAll(String key, Map<String, Object> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 仅当 hashKey 不存在时才设置
     *
     * @param key     哈希表
     * @param hashKey 字段名
     * @param value   值
     * @return 设置成功返回 true, 设置失败返回 false
     */
    public Boolean hPutIfAbsent(String key, String hashKey, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 删除哈希表中一个或多个字段
     *
     * @param key    哈希表
     * @param fields 要删除的字段集合
     * @return 删除成功的数目
     */
    public Long hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 查看哈希表中指定的字段是否存在
     *
     * @param key   要查看的哈希表
     * @param field 字段名
     * @return 存在返回 true, 不存在返回 false
     */
    public boolean hExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key       指定的哈希表
     * @param field     字段名
     * @param increment 增加的量
     * @return 增加后的值
     */
    public Long hIncrBy(String key, Object field, long increment) {
        return redisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment( 浮点型 )
     *
     * @param key   指定的哈希表
     * @param field 字段名
     * @param delta 增加的量( 浮点型 )
     * @return 增加后的值
     */
    public Double hIncrByFloat(String key, Object field, double delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key 哈希表
     * @return 所有的 字段
     */
    public Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key 哈希表
     * @return 哈希表所有字段的数量
     */
    public Long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key 哈希表
     * @return 哈希表中所有的值
     */
    public List<Object> hValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 迭代哈希表中的键值对
     *
     * @param key     哈希表
     * @param options 迭代的限制条件, 为 ScanOptions.NONE 则无限制
     * @return 下一个键值对元组的游标
     */
    public Cursor<Entry<Object, Object>> hScan(String key, ScanOptions options) {
        return redisTemplate.opsForHash().scan(key, options);
    }

    // ------------------------list 相关操作----------------------------

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   元素所在的列表
     * @param index 下标, 从 0 开始
     * @return 列表中指定下标的值
     */
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   元素所在列表
     * @param start 开始位置, 0 是开始位置
     * @param end   结束位置, -1 返回所有
     * @return 指定索引范围内的元素
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 存储在 list 头部( 左边 )
     *
     * @param key   列表
     * @param value 存储的值
     * @return 列表长度
     */
    public Long lLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 将多个值存入列表中
     *
     * @param key   列表
     * @param value 值, 可以输入多个
     * @return 列表长度
     */
    public Long lLeftPushAll(String key, Object... value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 将多个值存入列表中
     *
     * @param key   列表
     * @param value 值的集合
     * @return 列表长度
     */
    public Long lLeftPushAll(String key, Collection<Object> value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 当 list 存在的时候才加入
     *
     * @param key   列表
     * @param value 值
     * @return 列表长度
     */
    public Long lLeftPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 如果 pivot 存在,在 pivot 前面添加
     *
     * @param key   列表
     * @param pivot 基准值
     * @param value 要添加的值
     * @return 列表长度
     */
    public Long lLeftPush(String key, Object pivot, Object value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 存储在 list 尾部( 右边 )
     *
     * @param key   列表
     * @param value 值
     * @return 列表长度
     */
    public Long lRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将多个值存入列表中
     *
     * @param key   列表
     * @param value 值, 可以输入多个
     * @return 列表长度
     */
    public Long lRightPushAll(String key, Object... value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 将多个值存入列表中
     *
     * @param key   列表
     * @param value 值的集合
     * @return 列表长度
     */
    public Long lRightPushAll(String key, Collection<Object> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 为已存在的列表添加值
     *
     * @param key   存在的列表
     * @param value 值
     * @return 列表长度
     */
    public Long lRightPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 在 pivot 元素的右边添加值
     *
     * @param key   列表
     * @param pivot 基准值
     * @param value 要添加的值
     * @return 列表长度
     */
    public Long lRightPush(String key, Object pivot, Object value) {
        return redisTemplate.opsForList().rightPush(key, pivot, value);
    }

    /**
     * 通过索引设置列表元素的值
     *
     * @param key   列表
     * @param index 位置
     * @param value 值
     */
    public void lSet(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移出并获取列表的第一个元素
     *
     * @param key 列表
     * @return 删除的元素
     */
    public Object lLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素, 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     列表
     * @param timeout 等待时间
     * @param unit    时间单位
     * @return 删除的元素
     */
    public Object lBLeftPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表最后一个元素
     *
     * @param key 列表
     * @return 删除的元素
     */
    public Object lRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素, 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     列表
     * @param timeout 等待时间
     * @param unit    时间单位
     * @return 删除的元素
     */
    public Object lBRightPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      要移除元素的列表
     * @param destinationKey 要添加元素的列表
     * @return 移动的元素
     */
    public Object lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它; 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey      要移除元素的列表
     * @param destinationKey 要添加元素的列表
     * @param timeout        等待时间
     * @param unit           时间单位
     * @return 移动的元素
     */
    public Object lBRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
    }

    /**
     * 删除集合中值等于 value 的元素
     *
     * @param key   列表
     * @param index index = 0, 删除所有值等于value的元素;
     *              index > 0, 从头部开始删除第一个值等于 value 的元素;
     *              index < 0, 从尾部开始删除第一个值等于 value 的元素;
     * @param value 值
     * @return 列表长度
     */
    public Long lRemove(String key, long index, Object value) {
        return redisTemplate.opsForList().remove(key, index, value);
    }

    /**
     * 裁剪 list
     *
     * @param key   列表
     * @param start 起始位置
     * @param end   结束位置
     * @see <a href="https://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     */
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 获取列表长度
     *
     * @param key 列表
     * @return 列表长度
     */
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // --------------------set 相关操作--------------------------

    /**
     * set 添加元素
     *
     * @param key    集合
     * @param values 值, 可以同时添加多个
     * @return 集合长度
     */
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * set 移除元素
     *
     * @param key    集合
     * @param values 要移除的元素, 可以同时移除多个
     * @return 集合长度
     */
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param key 集合
     * @return 集合中随机一个元素
     */
    public Object sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 将元素 value 从一个集合移到另一个集合
     *
     * @param key     被移除的集合
     * @param value   要移除的元素
     * @param destKey 移动到的目标集合
     * @return 移动成功返回 true, 移动失败返回 false
     */
    public Boolean sMove(String key, Object value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     *
     * @param key 集合
     * @return 集合长度
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 判断集合是否包含 value
     *
     * @param key   集合
     * @param value 元素
     * @return 包含返回 true, 不包含返回 false
     */
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取两个集合的交集
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @return 两个集合的交集
     */
    public Set<Object> sIntersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 获取 key 集合与多个集合的交集
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @return 多个集合的交集
     */
    public Set<Object> sIntersect(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    /**
     * key 集合与 otherKey 集合的交集存储到 destKey 集合中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey,
                destKey);
    }

    /**
     * key 集合与多个集合的交集存储到 destKey 集合中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Long sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 获取两个集合的并集
     *
     * @param key       集合1
     * @param otherKeys 集合2
     * @return 两个集合的并集
     */
    public Set<Object> sUnion(String key, String otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 获取 key 集合与多个集合的并集
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @return 多个集合的并集
     */
    public Set<Object> sUnion(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * key 集合与 otherKey 集合的并集存储到 destKey 中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key 集合与多个集合的并集存储到 destKey 中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Long sUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @return 集合1 - 集合2 的差集
     */
    public Set<Object> sDifference(String key, String otherKey) {
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * 获取 key 集合与多个集合的差集
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @return 集合1 - 集合2 - 集合3 - ... 集合n 的差集
     */
    public Set<Object> sDifference(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * key 集合与 otherKey 集合的差集存储到 destKey 中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Long sDifference(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey,
                destKey);
    }

    /**
     * key 集合与多个集合的差集存储到 destKey 中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Long sDifference(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 获取集合所有元素
     *
     * @param key 集合
     * @return 集合中所有元素
     */
    public Set<Object> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取集合中的一个元素
     *
     * @param key 集合
     * @return 集合中随机一个元素
     */
    public Object sRandomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取集合中 count 个元素
     *
     * @param key   集合
     * @param count 要获取的元素个数
     * @return count 个随机元素组成的集合
     */
    public List<Object> sRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取集合中 count 个元素并且去除重复的
     *
     * @param key   集合
     * @param count 要获取的元素个数
     * @return count 个随机元素组成的集合, 并且不包含重复元素
     */
    public Set<Object> sDistinctRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 迭代集合中的元素
     *
     * @param key     集合
     * @param options 迭代的限制条件, 为 ScanOptions.NONE 则无限制
     * @return 下一个元素的游标
     */
    public Cursor<Object> sScan(String key, ScanOptions options) {
        return redisTemplate.opsForSet().scan(key, options);
    }

    // ------------------zSet 相关操作--------------------------------

    /**
     * 添加元素, 有序集合是按照元素的 score 值由小到大排列
     *
     * @param key   有序集合
     * @param value 元素
     * @param score 分数
     * @return 添加成功返回 true, 添加失败返回 false
     */
    public Boolean zAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 添加多个元素到有序集合中
     *
     * @param key    有序集合
     * @param values 多个元素值
     * @return 有序集合长度
     */
    public Long zAdd(String key, Set<TypedTuple<Object>> values) {
        return redisTemplate.opsForZSet().add(key, values);
    }

    /**
     * 移除有序集合中的值
     *
     * @param key    有序集合
     * @param values 要移除的值, 可以同时移除多个
     * @return 有序集合长度
     */
    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 增加元素的 score 值，并返回增加后的值
     *
     * @param key   有序集合
     * @param value 要增加的元素
     * @param delta 增加的分数是多少
     * @return 增加后的分数
     */
    public Double zIncrementScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的 score 值由小到大排列
     *
     * @param key   有序集合
     * @param value 值
     * @return 排名, 从小到大顺序, 0 表示第一位
     */
    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的 score 值由大到小排列
     *
     * @param key   有序集合
     * @param value 值
     * @return 排名, 从大到小顺序
     */
    public Long zReverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置, -1 表示从开始位置开始后面的所有元素
     * @return 指定区间的值的集合
     */
    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取集合元素, 并且把 score 值也获取
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置, -1 表示从开始位置开始后面的所有元素
     * @return 指定区间的元素及分数的元组的集合
     */
    public Set<TypedTuple<Object>> zRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 根据 score 值查询集合元素
     *
     * @param key 有序集合
     * @param min 最小值
     * @param max 最大值
     * @return 分数 在最小值与最大值之间的元素集合
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 根据 score 值查询集合元素及其分数, 并按分数从小到大排序
     *
     * @param key 有序集合
     * @param min 最小值
     * @param max 最大值
     * @return 分数 在最小值与最大值之间的元素与分数的元组的集合
     */
    public Set<TypedTuple<Object>> zRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 根据 score 值查询集合元素及其分数, 从小到大排序, 只获取 start 到 end 位置之间的结果
     *
     * @param key   有序集合
     * @param min   最低分数
     * @param max   最高分数
     * @param start 开始位置
     * @param end   结束位置
     * @return 分数在 min 与 max 之间, 位置在 start 与 end 之间的元素与分数的元组的集合
     */
    public Set<TypedTuple<Object>> zRangeByScoreWithScores(String key, double min, double max, long start, long end) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置
     * @return 按照 分数 倒序的元素集合
     */
    public Set<Object> zReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回 score 值
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定区间的元素及其分数的元组的集合
     */
    public Set<TypedTuple<Object>> zReverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 根据 score 值查询集合元素, 从大到小排序
     *
     * @param key 有序集合
     * @param min 分数最小值
     * @param max 分数最大值
     * @return 分数在 min 与 max 之间的元素的集合, 按分数倒序
     */
    public Set<Object> zReverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 根据 score 值查询集合元素, 从大到小排序
     *
     * @param key 有序集合
     * @param min 分数最小值
     * @param max 分数最大值
     * @return 分数在 min 与 max 之间的元素与分数的元组的集合, 按分数倒序
     */
    public Set<TypedTuple<Object>> zReverseRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 根据 score 值查询集合元素及其分数, 从小到大排序, 只获取 start 到 end 位置之间的结果, 按分数从小到大排序
     *
     * @param key   有序集合
     * @param min   分数最小值
     * @param max   分数最大值
     * @param start 开始位置
     * @param end   结束位置
     * @return 分数在 min 与 max 之间, 位置在 start 与 end 之间的元素与分数的元组的集合, 按分数倒序
     */
    public Set<Object> zReverseRangeByScore(String key, double min, double max, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
    }

    /**
     * 根据 score 值获取集合元素数量
     *
     * @param key 有序集合
     * @param min 分数最小值
     * @param max 分数最大值
     * @return 分数在最小值与最大值之间的元素数量
     */
    public Long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 获取集合大小( 底层实现还是 zcard )
     *
     * @param key 有序集合
     * @return 集合中的元素数量
     */
    public Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取集合大小
     *
     * @param key 有序集合
     * @return 集合中的元素数量
     */
    public Long zZCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 获取集合中 value 元素的 score 值
     *
     * @param key   有序集合
     * @param value 元素值
     * @return 该元素值的分数
     */
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 移除指定索引位置的成员
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置
     * @return 移除的元素个数
     */
    public Long zRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 根据指定的 score 值的范围来移除成员
     *
     * @param key 有序集合
     * @param min 分数最小值
     * @param max 分数最大值
     * @return 移除的元素个数
     */
    public Long zRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 获取 key 和 otherKey 的并集并存储在 destKey 中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Long zUnionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * 获取 key 和 otherKeys 的并集并存储在 destKey 中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Long zUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取 key 和 otherKey 的交集并存储在 destKey 中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Long zIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 获取 key 和 otherKeys 的交集并存储在 destKey 中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Long zIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 迭代有序集合
     *
     * @param key 有序集合
     * @param options 迭代限制条件, 为 ScanOptions.NONE 则无限制
     * @return 下一个元素及分数元组的游标
     */
    public Cursor<TypedTuple<Object>> zScan(String key, ScanOptions options) {
        return redisTemplate.opsForZSet().scan(key, options);
    }
}
