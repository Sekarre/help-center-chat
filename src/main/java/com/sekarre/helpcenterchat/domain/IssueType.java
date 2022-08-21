package com.sekarre.helpcenterchat.domain;

import com.sekarre.helpcenterchat.domain.enums.IssueTypeName;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private IssueTypeName name;

    @Builder.Default
    private boolean isAvailable = true;
}
