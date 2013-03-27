package yalp.db.jpa;

import javax.persistence.Query;

/**
 * Use yalp.db.jpa.GenericModel insteads
 */
@Deprecated
public class JPASupport extends GenericModel {

    /**
     * Use yalp.db.jpa.GenericModel.JPAQuery insteads
     */
    @Deprecated
    public static class JPAQuery extends GenericModel.JPAQuery {

        public JPAQuery(String sq, Query query) {
            super(sq, query);
        }

        public JPAQuery(Query query) {
            super(query);
        }
    }

}
