package com.central.webboard.board.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private String boardCode;
    private String memberCode;
    private String boardTitle;
    private String boardContext;
    private String boardCreateAt;
    private String boardUpdateAt;
}
