package com.example.demo.enums;

import java.util.EnumSet;
import java.util.Set;

public enum TenantStatus {
    Labs {
        @Override
        public Set<TenantStatus> nextStatus() {
            return EnumSet.of(Active, Locked, Expired, Suspended, Maintenance);
        }
    },
    Trial {
        @Override
        public Set<TenantStatus> nextStatus() {
            return EnumSet.of(Active, Locked, Expired, Suspended, Maintenance);
        }
    },
    Active {
        @Override
        public Set<TenantStatus> nextStatus() {
            return EnumSet.of(Locked, Expired, Suspended, Maintenance);
        }
    },
    Locked {
        @Override
        public Set<TenantStatus> nextStatus() {
            return EnumSet.of(Expired, Suspended, Maintenance);
        }
    },
    Expired {
        @Override
        public Set<TenantStatus> nextStatus() {
            return EnumSet.of(Locked, Expired, Suspended, Maintenance);
        }
    },
    Suspended {
        @Override
        public Set<TenantStatus> nextStatus() {
            return EnumSet.of(Active);
        }
    },
    Maintenance {
        @Override
        public Set<TenantStatus> nextStatus() {
            return EnumSet.of(Active, Locked);
        }
    },
    Inactive {
        @Override
        public Set<TenantStatus> nextStatus() {
            return EnumSet.of(Active);
        }
    };

    public Set<TenantStatus> nextStatus() {
        return EnumSet.noneOf(TenantStatus.class);
    }

    public static void main(String[] args) {
        System.out.println(TenantStatus.Maintenance.nextStatus());
    }

}
