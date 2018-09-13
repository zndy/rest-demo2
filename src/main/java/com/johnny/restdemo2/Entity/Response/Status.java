package com.johnny.restdemo2.Entity.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class Status {
    public enum ResponseType {
        SUCCESSFUL, FAILED, ENDED_PREMATURELY, FINISHED
    }

    private ResponseType status;
}
