/*
 * This file is generated by jOOQ.
 */
package ru.tinkoff.edu.java.scrapper.domain.jooq;


import javax.annotation.processing.Generated;

import org.jooq.Sequence;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;


/**
 * Convenience access to all sequences in public.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.17.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>public.domain_id_seq</code>
     */
    public static final Sequence<Long> DOMAIN_ID_SEQ = Internal.createSequence("domain_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);

    /**
     * The sequence <code>public.link_id_seq</code>
     */
    public static final Sequence<Long> LINK_ID_SEQ = Internal.createSequence("link_id_seq", Public.PUBLIC, SQLDataType.BIGINT.nullable(false), null, null, null, null, false, null);
}
