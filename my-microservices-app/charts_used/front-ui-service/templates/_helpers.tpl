{{/*
Expand the name of the chart.
*/}}
{{- define "front-ui-service.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "front-ui-service.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{- define "front-ui-service.shortname" -}}
{{- $full := include "front-ui-service.fullname" . -}}
{{- $prefix := printf "%s-" .Release.Name -}}
{{- $trimmedPrefix := trimPrefix $prefix $full -}}
{{- $shortname := trimSuffix "-service" $trimmedPrefix -}}
{{- $shortname }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "front-ui-service.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "front-ui-service.labels" -}}
helm.sh/chart: {{ include "front-ui-service.chart" . }}
{{ include "front-ui-service.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "front-ui-service.selectorLabels" -}}
app.kubernetes.io/name: {{ include "front-ui-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "front-ui-service.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "front-ui-service.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }} 