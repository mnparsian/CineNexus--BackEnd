package com.cinenexus.backend.model.whatchlist;

import com.cinenexus.backend.enumeration.WatchlistStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "watch_list_status")
public class WatchlistStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private WatchlistStatusType name;
}
