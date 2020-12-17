package com.wnn.system.domain.system;

import java.util.ArrayList;
import java.util.List;

public class RolePermExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RolePermExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andRolePermIdIsNull() {
            addCriterion("rolePermId is null");
            return (Criteria) this;
        }

        public Criteria andRolePermIdIsNotNull() {
            addCriterion("rolePermId is not null");
            return (Criteria) this;
        }

        public Criteria andRolePermIdEqualTo(String value) {
            addCriterion("rolePermId =", value, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdNotEqualTo(String value) {
            addCriterion("rolePermId <>", value, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdGreaterThan(String value) {
            addCriterion("rolePermId >", value, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdGreaterThanOrEqualTo(String value) {
            addCriterion("rolePermId >=", value, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdLessThan(String value) {
            addCriterion("rolePermId <", value, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdLessThanOrEqualTo(String value) {
            addCriterion("rolePermId <=", value, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdLike(String value) {
            addCriterion("rolePermId like", value, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdNotLike(String value) {
            addCriterion("rolePermId not like", value, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdIn(List<String> values) {
            addCriterion("rolePermId in", values, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdNotIn(List<String> values) {
            addCriterion("rolePermId not in", values, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdBetween(String value1, String value2) {
            addCriterion("rolePermId between", value1, value2, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRolePermIdNotBetween(String value1, String value2) {
            addCriterion("rolePermId not between", value1, value2, "rolePermId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("roleId is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("roleId is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(String value) {
            addCriterion("roleId =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(String value) {
            addCriterion("roleId <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(String value) {
            addCriterion("roleId >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(String value) {
            addCriterion("roleId >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(String value) {
            addCriterion("roleId <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(String value) {
            addCriterion("roleId <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLike(String value) {
            addCriterion("roleId like", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotLike(String value) {
            addCriterion("roleId not like", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<String> values) {
            addCriterion("roleId in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<String> values) {
            addCriterion("roleId not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(String value1, String value2) {
            addCriterion("roleId between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(String value1, String value2) {
            addCriterion("roleId not between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andPermIdIsNull() {
            addCriterion("permId is null");
            return (Criteria) this;
        }

        public Criteria andPermIdIsNotNull() {
            addCriterion("permId is not null");
            return (Criteria) this;
        }

        public Criteria andPermIdEqualTo(String value) {
            addCriterion("permId =", value, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdNotEqualTo(String value) {
            addCriterion("permId <>", value, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdGreaterThan(String value) {
            addCriterion("permId >", value, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdGreaterThanOrEqualTo(String value) {
            addCriterion("permId >=", value, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdLessThan(String value) {
            addCriterion("permId <", value, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdLessThanOrEqualTo(String value) {
            addCriterion("permId <=", value, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdLike(String value) {
            addCriterion("permId like", value, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdNotLike(String value) {
            addCriterion("permId not like", value, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdIn(List<String> values) {
            addCriterion("permId in", values, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdNotIn(List<String> values) {
            addCriterion("permId not in", values, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdBetween(String value1, String value2) {
            addCriterion("permId between", value1, value2, "permId");
            return (Criteria) this;
        }

        public Criteria andPermIdNotBetween(String value1, String value2) {
            addCriterion("permId not between", value1, value2, "permId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}