/**
 * CrudDao.java
 * Created: Feb 9, 2009 10:00:00 AM
 */
package cfd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Id;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.kompakar.ehealth.base.bo.BaseBo;
import com.kompakar.ehealth.base.bo.IHybridAware;
import com.kompakar.ehealth.base.cache.SimpleCache;
import com.kompakar.ehealth.base.cache.SimpleCacheItem;
import com.kompakar.ehealth.base.cache.SimpleCacheManager;
import com.kompakar.ehealth.frame.communicate.SessionFactory;
import com.kompakar.ehealth.frame.session.Session;

/**
 * <p>Project: com.kompakar.ehealth.frame</p>
 * <p>Description: 用于处理Crud的公用Dao泛型类</p>
 * <p>Copyright (c) 2005-2009 Kompakar Technology (Shanghai) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:gaoyuxiang@kompakar.com.cn">Gao Yuxiang</a>
 */
public class CrudDao<T extends IHybridAware> extends HibernateDaoSupport {
    private static Set<String> defaultUsingCacheList = new TreeSet<String>();

    static {
        String[] array = new String[] { "AccommCharge", //
                "AccommMstr", //
                "BaseUnitPrice", //
                "BedMstr", //
                "CareProvider", //
                "CareProviderProfile", //
                "ChargeItemMstr", //
                "CityMstr", //
                "CodeMstr", //
                "CommonSearch", //
                "CountryMstr", //
                "CustomerClassMstr", //
                "DepartmentGroupMstr", //
                "DepartmentMstr", //
                "DistrictMstr", //
                "DrugMstr", //
                "Employee", //
                "LocationMstr", //
                "MaterialItemMstr", //
                "MaterialItemUom", //
                "OrderItemMstr", //
                "OrderPerformLocationMstr", //
                "Organisation", //
                "Parameter", //
                "Patient", //
                "PatientAccount", //
                "Person", //
                "PositionMstr", //
                "RevenueCentreMstr", //
                "RoleMstr", //
                "RoomMstr", //
                "SpecialtyMstr", //
                "StakeHolder", //
                "StateMstr", //
                "StoreMstr", //
                "SubspecialtyMstr", //
                "SysFunctionMstr", //
                "SysFunctionPage", //
                "SysFunctionProfile", //
                "TxnCodeMstr", //
                "TxnCodeMstrEntity", //
                "UserMstr", //
                "UserProfile", //
                "VendorMstr", //
                "Visit", //
                "VisitCateGoryMstr", //
                "VisitRegnType", //
                "WardMstr", //
                "WardSubspecialty", //
        };

        defaultUsingCacheList.addAll(Arrays.asList(array));
    }

    private Class<T> clazz;

    public CrudDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * <p>Description: 获取后台HibernateTemplate对象以执行CrudDao标准接口外的Hibernate调用，除非特别必要，不建议使用。</p>
     * @return
     */
    public HibernateTemplate getTemplate() {
        HibernateTemplate tmpl = getHibernateTemplate();
        tmpl.setAlwaysUseNewSession(true);
        tmpl.setFlushMode(HibernateTemplate.FLUSH_EAGER);
        return tmpl;
    }

    /**
     * <p>Description: 创建记录</p>
     * @param entity
     */
    @Transactional
    public void insert(T entity) {
        setEntityMstrId(entity);

        long t1 = System.currentTimeMillis();
        getTemplate().save(entity);
        getTemplate().
        long t2 = System.currentTimeMillis();

        Session session = SessionFactory.getInstance().getCurrentSession();
        if (session != null) {
            if (session.isUsingModelCache() && this.isDefaultUsingCache()) {
                session.setUsingModelCache(false); // 后续的Crud操作禁用缓存
            }

            if (session.isLogInd()) {
                session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                session.setDbInsertOrUpadteCount(session.getDbInsertOrUpadteCount() + 1);
                session.addConnInfo(clazz, "insert", t2 - t1);
            }
        }
    }

    /**
     * <p>Description: 更新记录</p>
     * @param entity
     */
    @Transactional
    public void update(T entity) {
        long t1 = System.currentTimeMillis();
        getTemplate().update(entity);
        getTemplate().
        long t2 = System.currentTimeMillis();

        Session session = SessionFactory.getInstance().getCurrentSession();
        if (session != null) {
            if (session.isUsingModelCache() && this.isDefaultUsingCache()) {
                session.setUsingModelCache(false); // 后续的Crud操作禁用缓存
            }

            if (session.isLogInd()) {
                session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                session.setDbInsertOrUpadteCount(session.getDbInsertOrUpadteCount() + 1);
                session.addConnInfo(clazz, "update", t2 - t1);
            }
        }
    }

    /**
     * <p>Description: 先判断要比较字段的数据库值和传入值是否一样，一样的话继续更新，有变化不更新</p>
     * @param entity
     * @param compareProperty 指定的要比较的字段
     * @param compareValue 
     * @return 更新返回True，校验不通过不更新返回False
     */
    @Transactional
    public boolean updateWithCheck(T entity, String compareProperty, Object compareValue) {
        BigDecimal idValue = getIdValue(entity);
        if (idValue == null) {
            throw new IllegalArgumentException();
        }

        T dbEntity = get(idValue);
        Object dbPropertyValue = getPropertyValue(dbEntity, compareProperty);
        if ((dbPropertyValue == null && compareValue == null)
                || (dbPropertyValue != null && dbPropertyValue.equals(compareValue))) {
            long t1 = System.currentTimeMillis();
            getTemplate().update(entity);
            long t2 = System.currentTimeMillis();

            Session session = SessionFactory.getInstance().getCurrentSession();
            if (session != null) {
                if (session.isUsingModelCache() && this.isDefaultUsingCache()) {
                    session.setUsingModelCache(false); // 后续的Crud操作禁用缓存
                }

                if (session.isLogInd()) {
                    session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                    session.setDbInsertOrUpadteCount(session.getDbInsertOrUpadteCount() + 1);
                    session.addConnInfo(clazz, "update", t2 - t1);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Description: 先判断要比较字段的数据库值和传入值是否一样，一样的话继续更新，有变化不更新</p>
     * @param entity
     * @param comparePropertyList 指定的要比较的字段表
     * @param compareValueList
     * @return 更新返回True，校验不通过不更新返回False
     */
    @Transactional
    public boolean updateWithCheck(T entity, List<String> comparePropertyList, List<Object> compareValueList) {
        BigDecimal idValue = getIdValue(entity);
        if (idValue == null) {
            throw new IllegalArgumentException();
        }

        T dbEntity = get(idValue);
        boolean checkResult = true;
        for (int i = 0; i < comparePropertyList.size(); i++) {
            Object dbPropertyValue = getPropertyValue(dbEntity, comparePropertyList.get(i));
            Object compareValue = compareValueList.get(i);

            if ((dbPropertyValue == null && compareValue != null)
                    || (dbPropertyValue != null && dbPropertyValue.equals(compareValue) == false)) {
                checkResult = false;
                break;
            }
        }

        if (checkResult) {
            long t1 = System.currentTimeMillis();
            getTemplate().update(entity);
            long t2 = System.currentTimeMillis();

            Session session = SessionFactory.getInstance().getCurrentSession();
            if (session != null) {
                if (session.isUsingModelCache() && this.isDefaultUsingCache()) {
                    session.setUsingModelCache(false); // 后续的Crud操作禁用缓存
                }

                if (session.isLogInd()) {
                    session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                    session.setDbInsertOrUpadteCount(session.getDbInsertOrUpadteCount() + 1);
                    session.addConnInfo(clazz, "update", t2 - t1);
                }
            }
        }
        return checkResult;
    }

    /**
     * <p>Description: 先判断要比较字段的数据库值和传入值是否一样，一样的话继续更新，有变化不更新</p>
     * @param entity
     * @param paramMap key=字段名，value=字段值
     * @return 更新返回True，校验不通过不更新返回False
     */
    @Transactional
    public boolean updateWithCheck(T entity, Map<String, Object> paramMap) {
        BigDecimal idValue = getIdValue(entity);
        if (idValue == null) {
            throw new IllegalArgumentException();
        }

        T dbEntity = get(idValue);
        boolean checkResult = true;
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            Object dbPropertyValue = getPropertyValue(dbEntity, entry.getKey());
            Object compareValue = entry.getValue();

            if ((dbPropertyValue == null && compareValue != null)
                    || (dbPropertyValue != null && dbPropertyValue.equals(compareValue) == false)) {
                checkResult = false;
                break;
            }
        }

        if (checkResult) {
            long t1 = System.currentTimeMillis();
            getTemplate().update(entity);
            long t2 = System.currentTimeMillis();

            Session session = SessionFactory.getInstance().getCurrentSession();
            if (session != null) {
                if (session.isUsingModelCache() && this.isDefaultUsingCache()) {
                    session.setUsingModelCache(false);
                }

                if (session.isLogInd()) {
                    session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                    session.setDbInsertOrUpadteCount(session.getDbInsertOrUpadteCount() + 1);
                    session.addConnInfo(clazz, "update", t2 - t1);
                }
            }
        }
        return checkResult;
    }

    /**
     * <p>Description: 删除记录</p>
     * @param entity
     */
    @Transactional
    public void delete(T entity) {
        long t1 = System.currentTimeMillis();
        getTemplate().delete(entity);
        long t2 = System.currentTimeMillis();

        Session session = SessionFactory.getInstance().getCurrentSession();
        if (session != null) {
            if (session.isUsingModelCache() && this.isDefaultUsingCache()) {
                session.setUsingModelCache(false); // 后续的Crud操作禁用缓存
            }

            if (session.isLogInd()) {
                session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                session.addConnInfo(clazz, "delete", t2 - t1);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private T internalGetFromDB(BigDecimal id) {
        if (id == null) {
            return null;
        }

        long t1 = System.currentTimeMillis();
        T entity = (T) getTemplate().get(clazz, id);
        long t2 = System.currentTimeMillis();

        if (entity != null) {
            entity.resetModifiedFlag();
        }

        Session session = SessionFactory.getInstance().getCurrentSession();
        if (session != null) {
            if (session.isLogInd()) {
                session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                session.setDbSelectCount(session.getDbSelectCount() + 1);
                session.addConnInfo(clazz, "get", t2 - t1);
            }
        }
        return entity;
    }

    /**
     * <p>Description: 根据ID获取记录</p>
     * @param id
     * @return
     */
    public T get(BigDecimal id) {
        if (this.isUsingCache()) {
            return this.getAndCache(id);
        } else {
            return this.internalGetFromDB(id);
        }
    }

    /**
     * 通过ID获得对象，并缓存。
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public T getAndCache(BigDecimal id) {
        if (id == null) {
            return null;
        }

        Session session = SessionFactory.getInstance().getCurrentSession();
        if (session == null) { // 无会话则直接从数据库获取
            return internalGetFromDB(id);
        }

        final String cacheName = this.clazz.getName();
        final String cacheKey = id.toString();

        final SimpleCacheManager cacheManager = session.getModelCacheManager();
        final SimpleCache<T> cache = cacheManager.getOrCreateCache(cacheName);
        final SimpleCacheItem<T> cacheItem = cache.get(cacheKey);

        if (cacheItem == null) {
            final T value = this.internalGetFromDB(id);
            cache.put(cacheKey, value); // 注意：null也会被缓存
            return value;
        } else {
            return cacheItem.getValue();
        }
    }

    private boolean isDefaultUsingCache() {
        return defaultUsingCacheList.contains(this.clazz.getSimpleName());
    }

    private boolean isUsingCache() {
        Session session = SessionFactory.getInstance().getCurrentSession();
        if (session == null) { // 无session，未登陆、未模拟session的WebService、未模拟session的eHealthTasks等情况
            return this.isDefaultUsingCache();
        } else { // 有session
            return session.isUsingModelCache() && this.isDefaultUsingCache();
        }
    }

    /**
     * <p>Description: 根据样本获取相似记录</p>
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T entity) {
        long t1 = System.currentTimeMillis();
        List<T> entityList = (List<T>) getTemplate().findByExample(entity);
        long t2 = System.currentTimeMillis();

        for (T item : entityList) {
            item.resetModifiedFlag();
        }

        Session session = SessionFactory.getInstance().getCurrentSession();
        if (session != null) {
            if (session.isLogInd()) {
                session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                session.setDbSelectCount(session.getDbSelectCount() + 1);
                session.addConnInfo(clazz, "findByExample", t2 - t1);
            }
        }
        return entityList;
    }

    /**
     * 查找单条记录。注意：如果多条结果也会返还空。
     *
     * @param entity
     * @return
     */
    public T findUnique(T entity) {
        List<T> list = this.findByExample(entity);
        return list != null && list.size() == 1 ? list.get(0) : null;
    }

    /**
     * <p>Description: 根据样本获取相似记录</p>
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findByExampleOfDefaultEntityMstrId(T entity) {
        setEntityMstrId(entity);

        long t1 = System.currentTimeMillis();
        List<T> entityList = (List<T>) getTemplate().findByExample(entity);
        long t2 = System.currentTimeMillis();

        for (T item : entityList) {
            item.resetModifiedFlag();
        }

        Session session = SessionFactory.getInstance().getCurrentSession();
        if (session != null) {
            if (session.isLogInd()) {
                session.setDbRequestTime(session.getDbRequestTime() + (t2 - t1));
                session.setDbSelectCount(session.getDbSelectCount() + 1);
                session.addConnInfo(clazz, "findByExample", t2 - t1);
            }
        }
        return entityList;
    }

    private BigDecimal getIdValue(T entity) {
        Field idField = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                idField = field;
                break;
            }
        }

        if (idField == null) {
            return null;
        }

        return (BigDecimal) getPropertyValue(entity, idField.getName());
    }

    private Object getPropertyValue(T entity, String propertyName) {
        try {
            String getterMethod = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            Method method = entity.getClass().getMethod(getterMethod, new Class[] {});
            return method.invoke(entity, new Object[] {});
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置实体类中的entityMstrId属性
     * @param entity
     */
    private void setEntityMstrId(T entity) {
        try {
            Session session = SessionFactory.getInstance().getCurrentSession();
            if (session != null && session.getEntityMstrId() != null //
                    && entity instanceof BaseBo && ((BaseBo) entity).isEnableDefaultEntityMstrId()) {
                // 取当前类的entityMstrId属性，注意BaseBo也有entityMstrId属性
                Field field = clazz.getDeclaredField("entityMstrId");
                field.setAccessible(true);
                if (field.get(entity) == null) { // 如果为空，则赋值
                    field.set(entity, session.getEntityMstrId());
                }
            }
        } catch (NoSuchFieldException e) { // 忽略异常
            e.printStackTrace();
        } catch (SecurityException e) { // 忽略异常
            e.printStackTrace();
        } catch (Exception e) { // 忽略异常
            e.printStackTrace();
        }
    }
}
